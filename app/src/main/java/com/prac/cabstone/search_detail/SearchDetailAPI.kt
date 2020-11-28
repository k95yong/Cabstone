package com.prac.cabstone.search_detail

import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.models.ResponseGetInfoForArea
import com.prac.cabstone.models.ResponseGetSearchDetail
import com.prac.cabstone.search_result.GetInfoForAreaCodeAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface SearchDetailAPI {
    @GET("/ko/detail/{id}")
    fun getSearchDetail(@Path("id") id: Int): Call<ResponseGetSearchDetail>

    @GET("/en/detail/{id}")
    fun getSearchDetail_en(@Path("id") id: Int): Call<ResponseGetSearchDetail>

    @GET("/jp/detail/{id}")
    fun getSearchDetail_jp(@Path("id") id: Int): Call<ResponseGetSearchDetail>

    @GET("/ch/detail/{id}")
    fun getSearchDetail_ch(@Path("id") id: Int): Call<ResponseGetSearchDetail>

    @GET("/ge/detail/{id}")
    fun getSearchDetail_ge(@Path("id") id: Int): Call<ResponseGetSearchDetail>

    @GET("/sp/detail/{id}")
    fun getSearchDetail_sp(@Path("id") id: Int): Call<ResponseGetSearchDetail>

    companion object {
        fun create(): SearchDetailAPI {
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
                .create(SearchDetailAPI::class.java)
        }
    }
}