package com.example.androidapp.networkService




import com.example.androidapp.home.model.NewsDAO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CommonApiService {

    @GET("/v2/top-headlines")
    suspend fun getData(@Query("country") country : String="us",@Query("category") category : String="business"
                                ,@Query("apiKey") apiKey : String="546ec8516779407f929e3c832549a769",) :Response<NewsDAO>
}