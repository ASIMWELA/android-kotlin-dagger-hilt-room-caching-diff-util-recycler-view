package news.my.kotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ArticleEntity::class], version = 1)
@TypeConverters(TypeConvertors::class)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun getArticlesDao() :ArticleDao
    companion object{
        val DATABASE_NAME = "articles.db"
    }
}