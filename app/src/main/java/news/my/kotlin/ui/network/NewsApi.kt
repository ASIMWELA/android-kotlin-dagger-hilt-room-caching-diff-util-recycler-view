package news.my.kotlin.ui.network

import news.my.kotlin.model.NewsResponse
import news.my.kotlin.utils.ApiResult
import news.my.kotlin.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun getAllNews(
        @Query("language") language: String = "en",
        @Query("country") country:String="us",
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getAllNewsFlow(
        @Query("language") language: String = "en",
        @Query("country") country:String="us",
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getAllNewsNetworkBound(
        @Query("language") language: String = "en",
        @Query("country") country:String="us",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

}