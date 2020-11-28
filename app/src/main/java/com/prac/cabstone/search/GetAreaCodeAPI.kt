package com.prac.cabstone.search

import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.models.ResponseGetAreaCode
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface GetAreaCodeAPI {
    @GET("/ko/area-code")
    fun getAreaCode(): Call<ResponseGetAreaCode>

    @GET("/en/area-code")
    fun getAreaCode_en(): Call<ResponseGetAreaCode>

    @GET("/jp/area-code")
    fun getAreaCode_jp(): Call<ResponseGetAreaCode>

    @GET("/ch/area-code")
    fun getAreaCode_ch(): Call<ResponseGetAreaCode>

    @GET("/ge/area-code")
    fun getAreaCode_ge(): Call<ResponseGetAreaCode>

    @GET("/sp/area-code")
    fun getAreaCode_sp(): Call<ResponseGetAreaCode>

    companion object {

        fun create(): GetAreaCodeAPI {
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
                .create(GetAreaCodeAPI::class.java)
        }
    }
}