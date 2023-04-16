package news.my.kotlin.db

import news.my.kotlin.model.Article

class EntityMapper {
    companion object {
        fun fromEntity(entity: ArticleEntity): Article {
            return Article(
                author = entity.author ?: "",
                content = entity.content ?: "",
                description = entity.description?:"",
                publishedAt = entity.publishedAt,
                source = entity.source,
                title = entity.title,
                url = entity.url,
                urlToImage = entity.urlToImage
            )
        }

        fun fromModel(model: Article): ArticleEntity {
            return ArticleEntity(
                author = model.author ?: "",
                content = model.content ?: "",
                description = model.description?:"",
                publishedAt = model.publishedAt,
                source = model.source,
                title = model.title,
                url = model.url,
                urlToImage = model.urlToImage
            )
        }

        fun fromModelList(models: List<Article>): List<ArticleEntity> {
            return models.map { fromModel(it) }
        }

        fun fromEntityList(models: List<ArticleEntity>): List<Article> {
            return models.map { fromEntity(it) }
        }
    }
}