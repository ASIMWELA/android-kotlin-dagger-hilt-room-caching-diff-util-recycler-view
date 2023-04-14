package news.my.kotlin.repository

import news.my.kotlin.ui.network.NewsApi

class NewsRepository(private val newsApi: NewsApi) {
    suspend fun getAllNews() = newsApi.getAllNews();
}