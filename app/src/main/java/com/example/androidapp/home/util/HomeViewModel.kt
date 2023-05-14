package com.example.androidapp.home.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidapp.home.model.NewsDAO
import com.example.androidapp.networkService.ApiResult
import com.example.androidapp.roomDb.NewsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository):ViewModel() {

    private val _newsData = MutableLiveData<ApiResult<NewsDAO>>()
    val newsData: MutableLiveData<ApiResult<NewsDAO>>
        get() = _newsData

     fun fetchNewsData() {
        _newsData.value = ApiResult.Loading
        viewModelScope.launch {
            _newsData.value=repository.getData()
        }
    }
    private val _savedNews=MutableLiveData<List<NewsModel>>()
    val savedNews:MutableLiveData<List<NewsModel>>
        get() = _savedNews

    fun getSavedNews(){
        viewModelScope.launch {
            repository.getAllData()
        }
    }
    fun deleteSaved()=viewModelScope.launch {
        repository.deleteAllData()
    }

    fun saveData(data:NewsDAO)=viewModelScope.launch {
        val list = ArrayList<NewsModel>()
        withContext(Dispatchers.IO) {
            for (item in data.articles) {
                list.add(
                    NewsModel(
                        source = item.source!!.id.toString(),
                        author = item.author.toString(),
                        title = item.title.toString(),
                        description = item.description.toString(),
                        url = item.url.toString(),
                        urlToImage = item.urlToImage.toString(),
                        publishedAt = item.publishedAt.toString(),
                        content = item.content.toString()
                    )
                )
            }
            repository.insertData(list)


        }
    }



}