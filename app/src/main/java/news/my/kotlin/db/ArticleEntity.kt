package news.my.kotlin.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import news.my.kotlin.model.Source

@Entity(tableName = "articles")
data class ArticleEntity(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}
