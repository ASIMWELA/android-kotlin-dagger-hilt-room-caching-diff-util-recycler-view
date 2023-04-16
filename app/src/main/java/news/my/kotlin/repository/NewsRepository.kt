package news.my.kotlin.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import news.my.kotlin.db.ArticleDao
import news.my.kotlin.db.EntityMapper
import news.my.kotlin.model.NewsResponse
import news.my.kotlin.ui.network.NewsApi
import news.my.kotlin.utils.ApiResult
import java.net.UnknownHostException

class NewsRepository(private val newsApi: NewsApi, private val articleDao: ArticleDao) {
    suspend fun getAllNews() = newsApi.getAllNews();

    suspend fun getAllNewsFLow(): Flow<ApiResult<NewsResponse>> = flow {
        try {
            val response = newsApi.getAllNewsFlow()
            if (response.isSuccessful) {
                val articles = response.body()?.articles
                val articlesEntities = articles?.let { EntityMapper.fromModelList(it) }
                articleDao.deleteArticles()
                articlesEntities?.let { articleDao.insertArticles(it) }
                emit(ApiResult.Success(response.body()))
            } else {
                val errorMessage = response.errorBody()!!.string()
                response.errorBody()!!.close()

                articleDao.getArticles().collect {
                    val articles = EntityMapper.fromEntityList(it)
                    val createdResponse = NewsResponse(articles, "error", articles.count())
                    emit(ApiResult.Error(errorMessage, createdResponse))
                }

            }
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                articleDao.getArticles().collect {
                    val articles = EntityMapper.fromEntityList(it)
                    val response = NewsResponse(articles, "error", articles.count())
                    emit(ApiResult.Error("Connection problem, viewing cached data", response))
                }

            } else {
                articleDao.getArticles().collect {
                    val articles = EntityMapper.fromEntityList(it)
                    val response = NewsResponse(articles, "error", articles.count())
                    e.message?.let { it1 -> ApiResult.Error(it1, response) }
                        ?.let { it2 -> emit(it2) }
                }
            }


        }


    }

}