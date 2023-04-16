package news.my.kotlin.ui.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import news.my.kotlin.model.NewsResponse
import news.my.kotlin.repository.NewsRepository
import news.my.kotlin.utils.ApiResult
import javax.inject.Inject

@HiltViewModel
class AllNewsViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

    private val _newsArticles: MutableLiveData<ApiResult<NewsResponse>> = MutableLiveData()

    val articles: LiveData<ApiResult<NewsResponse>>
        get() = _newsArticles

    init {
        getNewsFlow()
    }


    private fun getNewsFlow() = viewModelScope.launch {
       newsRepository.getAllNewsFLow().onStart { emit(ApiResult.Loading(true))}.flowOn(
           Dispatchers.IO
       ).collect{
            _newsArticles.value = it
       }


    }

    val newsAsLiveData = flow {
        val response = newsRepository.getAllNews()
        if (response.isSuccessful) {
            emit(ApiResult.Success(response.body()))   // 2. Success State
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            emit(errorMsg?.let { ApiResult.Error(it, null) })  // 3. Error State
        }

    }.onStart {
        emit(ApiResult.Loading(true))
    }.flowOn(Dispatchers.IO)
        .asLiveData()


}