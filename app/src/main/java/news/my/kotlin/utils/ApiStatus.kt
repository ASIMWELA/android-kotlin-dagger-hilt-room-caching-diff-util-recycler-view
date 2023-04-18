package news.my.kotlin.utils

enum class ApiStatus{
    SUCCESS,
    ERROR,
    LOADING
}  // for your case might be simplify to use only sealed class

sealed class ApiResult <out T> (val status: ApiStatus, val data: T?, val error:Throwable?) {

    data class Success<out R>(val _data: R?): ApiResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        error = null
    )

    data class Error<out R>(val exception: Throwable, val _data: R?): ApiResult<R>(
        status = ApiStatus.ERROR,
        data = _data,
        error = exception
    )

    data class Loading<out R>(val isLoading: Boolean): ApiResult<R>(
        status = ApiStatus.LOADING,
        data = null,
        error = null
    )
}