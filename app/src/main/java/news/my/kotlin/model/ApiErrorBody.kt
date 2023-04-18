package news.my.kotlin.model

data class ApiErrorBody(
    val code: String,
    val message: String,
    val status: String
)