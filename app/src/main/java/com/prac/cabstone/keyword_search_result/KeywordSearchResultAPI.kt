package com.prac.cabstone.keyword_search_result

import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.current_location_result.CurrentLocationAPI
import com.prac.cabstone.models.ResponseGetResultForCurrent
import com.prac.cabstone.models.ResponseGetResultForKeyword
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface KeywordSearchResultAPI {
    @GET("/ko/search-keyword")
    fun getResultForKeyword(
        @Query("keyword") mapX : String
    ): Call<ResponseGetResultForKeyword>

    @GET("/en/search-keyword")
    fun getResultForKeyword_en(
        @Query("keyword") mapX : String
    ): Call<ResponseGetResultForKeyword>

    @GET("/jp/search-keyword")
    fun getResultForKeyword_jp(
        @Query("keyword") mapX : String
    ): Call<ResponseGetResultForKeyword>

    @GET("/ch/search-keyword")
    fun getResultForKeyword_ch(
        @Query("keyword") mapX : String
    ): Call<ResponseGetResultForKeyword>

    @GET("/ge/search-keyword")
    fun getResultForKeyword_ge(
        @Query("keyword") mapX : String
    ): Call<ResponseGetResultForKeyword>

    @GET("/sp/search-keyword")
    fun getResultForKeyword_sp(
        @Query("keyword") mapX : String
    ): Call<ResponseGetResultForKeyword>

    companion object {

        fun create(): KeywordSearchResultAPI {
            val jwtToken = ApplicationClass.prefs.myJWT

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-ACCESS-TOKEN", jwtToken.toString())
                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(ApplicationClass.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KeywordSearchResultAPI::class.java)
        }
    }
}