package news.my.kotlin.ui.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import news.my.kotlin.repository.NewsRepository
import news.my.kotlin.utils.ApiResult
import javax.inject.Inject

@HiltViewModel
class AllNewsViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {
    val newsAsLiveData = flow {
        emit(ApiResult.Loading(null, true))   // 1. Loading State
        val response = newsRepository.getAllNews()
        if (response.isSuccessful) {
            emit(ApiResult.Success(response.body()))   // 2. Success State
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            emit(errorMsg?.let { ApiResult.Error(it) })  // 3. Error State
        }

    }.asLiveData()
}