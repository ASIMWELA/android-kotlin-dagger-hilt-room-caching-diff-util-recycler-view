package news.my.kotlin.ui.all

import android.os.Build
import androidx.annotation.RequiresApi
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
//
//    init {
//        getNewsFlow()
//    }


//    private fun getNewsFlow() = viewModelScope.launch {
//       newsRepository.getAllNewsFLow().onStart { emit(ApiResult.Loading(true))}.flowOn(
//           Dispatchers.IO
//       ).collect{
//            _newsArticles.value = it
//       }
//
//
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    val networkBoundNews = newsRepository.getArticlesNetworkBound().asLiveData()


}