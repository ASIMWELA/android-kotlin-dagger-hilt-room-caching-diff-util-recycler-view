package news.my.kotlin.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import news.my.kotlin.model.NewsResponse
import news.my.kotlin.ui.network.NewsApi
import retrofit2.Response

class NewsRepository(private val newsApi: NewsApi) {
    suspend fun getAllNews() = newsApi.getAllNews();

    suspend fun getAllNewsFlow() : Flow<Response<NewsResponse>> {
        return flow{
           val response = newsApi.getAllNewsFlow()
            emit(response)
        }.flowOn(Dispatchers.IO)

    }
}