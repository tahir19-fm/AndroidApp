package com.example.androidapp.networkService



import android.util.Log
import androidx.viewbinding.BuildConfig
import com.example.androidapp.AndroidApp
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient.Builder
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class OkHttpClientHelper {

    companion object {
        private const val ANDROID_EVENT = "android_event"
        const val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
        const val READ_TIMEOUT = "READ_TIMEOUT"
        const val WRITE_TIMEOUT = "WRITE_TIMEOUT"
        const val CACHED_TIME_SECONDS = "CACHED_TIME_SECONDS"
    }

    fun getOkHttpClient(): OkHttpClient {
        val httpCacheDirectory = File(AndroidApp.instance?.cacheDir, "http-cache")
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        val client: Builder = Builder()
            .addInterceptor(getHttpLoggingInterceptor())
            .addInterceptor(getRequestInterceptor())
        client.cache(cache)
        return client.build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Log.d("message", "getHttpLoggingInterceptor:${message} ") }
        if (BuildConfig.DEBUG)
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        return httpLoggingInterceptor
    }

    private fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            var connectTimeout = 30 * 1000
            var readTimeout = 30 * 1000
            var writeTimeout = 2 * 60 * 1000
            val connectNew = request.header(CONNECT_TIMEOUT)
            val readNew = request.header(READ_TIMEOUT)
            val writeNew = request.header(WRITE_TIMEOUT)
            val cachedTimeInSecs = request.header(CACHED_TIME_SECONDS)
            if (!connectNew.isNullOrEmpty()) {
                connectTimeout = connectNew.toInt()
            }
            if (!readNew.isNullOrEmpty()) {
                readTimeout = readNew.toInt()
            }
            if (!writeNew.isNullOrEmpty()) {
                Log.d("writeNew", "getRequestInterceptor: $writeNew")
                writeTimeout = writeNew.toInt()
            }
            try {
                var originalResponse = chain
                    .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                    .proceed(getHeaders(request))
                if (!cachedTimeInSecs.isNullOrEmpty()) {
                    val maxAge = cachedTimeInSecs.toInt()
                    originalResponse = originalResponse
                        .newBuilder()
                        .addHeader("Cache-Control", "public, max-age=$maxAge")
                        .build()
                }
                originalResponse
            } catch (e: SocketTimeoutException) {
                Response.Builder()
                    .request(request)
                    .code(999)
                    .protocol(Protocol.HTTP_1_1)
                    .body(
                        "{$e}".toResponseBody(
                            "".toRequestBody(("application/json").toMediaTypeOrNull()).contentType()
                        )
                    )
                    .message("socketTimeOut $e")
                    .build()
            } catch (e: UnknownHostException) {
                Response.Builder()
                    .request(request)
                    .code(999)
                    .protocol(Protocol.HTTP_1_1)
                    .body(
                        "{$e}".toResponseBody(
                            "".toRequestBody(("application/json").toMediaTypeOrNull()).contentType()
                        )
                    )
                    .message("unKnownHostException $e")
                    .build()
            }

        }
    }

    private fun getHeaders(request: Request): Request {
        val versionCode = "1"
        val authToken = "1234567890"
        val req = request.newBuilder()
            .header("APP-VERSION-CODE", versionCode)
            .header("Authorization", authToken)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
        return req.build()
    }
}