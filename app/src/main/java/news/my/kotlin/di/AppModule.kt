package news.my.kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import news.my.kotlin.db.ArticleDao
import news.my.kotlin.repository.NewsRepository
import news.my.kotlin.ui.network.NewsApi
import news.my.kotlin.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesNewsApi(): NewsApi {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsRepository(
        newsApi: NewsApi, newsDao: ArticleDao
    ): NewsRepository = NewsRepository(newsApi, newsDao)

}