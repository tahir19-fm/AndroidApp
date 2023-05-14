package com.example.androidapp.networkService


import retrofit2.Response

fun <T : Any> networkCall(response: Response<T>): ApiResult<T> {
    return if (response.isSuccessful) {
        response.body()?.let { ApiResult.Success(response.body()) }
            ?: ApiResult.Error("empty response body")
    }
    else
        ApiResult.Error(response.message())
}