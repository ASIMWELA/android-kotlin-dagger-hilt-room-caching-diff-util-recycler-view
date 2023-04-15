package news.my.kotlin.ui.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import news.my.kotlin.model.NewsResponse
import news.my.kotlin.repository.NewsRepository
import news.my.kotlin.utils.ApiResult
import javax.inject.Inject

@HiltViewModel
class AllNewsViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

    val newsLiveData: MutableLiveData<ApiResult<NewsResponse>> = MutableLiveData()

    init {
        getNewsFlow()
    }

    private fun getNewsFlow() = viewModelScope.launch {
        newsRepository.getAllNewsFlow().onStart {
            newsLiveData.postValue(ApiResult.Loading( true))
        }.collect { response ->
                if (response.isSuccessful) {
                    newsLiveData.postValue(ApiResult.Success(response.body()))  // 2. Success State
                } else {
                    val errorMsg = response.errorBody()?.string()
                    response.errorBody()
                        ?.close()  // remember to close it after getting the stream of error body
                    newsLiveData.postValue(errorMsg?.let { ApiResult.Error(it) })  // 3. Error State
                }

            }
    }


    val newsAsLiveData = flow {
        emit(ApiResult.Loading( true))   // 1. Loading State
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