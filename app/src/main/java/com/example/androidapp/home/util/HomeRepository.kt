package com.example.androidapp.home.util

import com.example.androidapp.home.model.NewsDAO
import com.example.androidapp.networkService.ApiResult
import com.example.androidapp.networkService.CommonApiService
import com.example.androidapp.networkService.networkCall
import com.example.androidapp.roomDb.AppDatabaseDao
import com.example.androidapp.roomDb.NewsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: CommonApiService,private val roomDb:AppDatabaseDao) {
    suspend fun getData(): ApiResult<NewsDAO> {
        return networkCall ( apiService.getData() )
    }
    suspend fun insertData(list: List<NewsModel>)=roomDb.insert(list)
    suspend fun getAllData()=roomDb.getList()
    suspend fun deleteAllData()=roomDb.deleteAll()
    suspend fun insertNews(newsModel: NewsModel)=roomDb.saveNews(newsModel)

}