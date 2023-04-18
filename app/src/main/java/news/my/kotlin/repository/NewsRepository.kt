package news.my.kotlin.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import news.my.kotlin.db.ArticleDao
import news.my.kotlin.db.ArticlesDatabase
import news.my.kotlin.db.EntityMapper
import news.my.kotlin.model.NewsResponse
import news.my.kotlin.ui.network.NewsApi
import news.my.kotlin.utils.ApiResult
import news.my.kotlin.utils.networkBoundResource
import java.net.UnknownHostException
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi,
                                         private val articlesDatabase: ArticlesDatabase,
                                         @ApplicationContext private val context: Context) {
    suspend fun getAllNews() = newsApi.getAllNews();

    private val articleDao = articlesDatabase.getArticlesDao();

//    suspend fun getAllNewsFLow(): Flow<ApiResult<NewsResponse>> = flow {
//        try {
//            val response = newsApi.getAllNewsFlow()
//            if (response.isSuccessful) {
//                val articles = response.body()?.articles
//                val articlesEntities = articles?.let { EntityMapper.fromModelList(it) }
//                articleDao.deleteArticles()
//                articlesEntities?.let { articleDao.insertArticles(it) }
//                emit(ApiResult.Success(response.body()))
//            } else {
//                val errorMessage = response.errorBody()!!.string()
//                response.errorBody()!!.close()
//
//                articleDao.getArticles().collect {
//                    val articles = EntityMapper.fromEntityList(it)
//                    val createdResponse = NewsResponse(articles, "error", articles.count())
//                    emit(ApiResult.Error(errorMessage, createdResponse))
//                }
//
//            }
//        } catch (e: Exception) {
//            if (e is UnknownHostException) {
//                articleDao.getArticles().collect {
//                    val articles = EntityMapper.fromEntityList(it)
//                    val response = NewsResponse(articles, "error", articles.count())
//                    emit(ApiResult.Error("Connection problem, viewing cached data", response))
//                }
//
//            } else {
//                articleDao.getArticles().collect {
//                    val articles = EntityMapper.fromEntityList(it)
//                    val response = NewsResponse(articles, "error", articles.count())
//                    e.message?.let { it1 -> ApiResult.Error(it1, response) }
//                        ?.let { it2 -> emit(it2) }
//                }
//            }
//
//
//        }
//
//
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getArticlesNetworkBound() = networkBoundResource(
        // pass in the logic to query data from the database
        query = {
            articleDao.getArticles()
        },
        // pass in the logic to fetch data from the api
        fetch = {
            newsApi.getAllNewsNetworkBound()
        },

        //pass in the logic to save the result to the local cache
        saveFetchResult = { articlesResponse ->
            val articlesEntities = EntityMapper.fromModelList(articlesResponse.articles)
            articlesDatabase.withTransaction {
                articleDao.deleteArticles()
                articleDao.insertArticles(articlesEntities)
            }
        },

        //pass in the logic to determine if the networking call should be made
        shouldFetch = {articlesResponse ->
            articlesResponse.isEmpty() or isOnline(context)
        }
    )
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


}