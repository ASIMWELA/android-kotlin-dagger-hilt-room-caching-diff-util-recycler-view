package news.my.kotlin.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import news.my.kotlin.db.ArticleDao
import news.my.kotlin.db.ArticlesDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModules {
    @Provides
    @Singleton
    fun providesArticlesDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ArticlesDatabase::class.java,
        ArticlesDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun providesArticlesDao(database: ArticlesDatabase):ArticleDao = database.getArticlesDao()
}