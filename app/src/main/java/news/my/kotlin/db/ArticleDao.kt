package news.my.kotlin.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Upsert
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Delete
    suspend fun deleteArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun deleteArticles()

    @Query("SELECT * FROM articles")
    fun getArticles(): Flow<List<ArticleEntity>>
}