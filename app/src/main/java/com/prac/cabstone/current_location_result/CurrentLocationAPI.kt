package com.prac.cabstone.current_location_result

import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.models.ResponseGetResultForCurrent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface CurrentLocationAPI {
    @GET("/ko/location-based-list")
    fun getResultForCurrent(
        @Query("mapX") mapX : Double,
        @Query("mapY") mapY : Double,
        @Query("radius") radius : Int
    ): Call<ResponseGetResultForCurrent>

    @GET("/en/location-based-list")
    fun getResultForCurrent_en(
        @Query("mapX") mapX : Double,
        @Query("mapY") mapY : Double,
        @Query("radius") radius : Int
    ): Call<ResponseGetResultForCurrent>

    @GET("/jp/location-based-list")
    fun getResultForCurrent_jp(
        @Query("mapX") mapX : Double,
        @Query("mapY") mapY : Double,
        @Query("radius") radius : Int
    ): Call<ResponseGetResultForCurrent>

    @GET("/ch/location-based-list")
    fun getResultForCurrent_ch(
        @Query("mapX") mapX : Double,
        @Query("mapY") mapY : Double,
        @Query("radius") radius : Int
    ): Call<ResponseGetResultForCurrent>

    @GET("/ge/location-based-list")
    fun getResultForCurrent_ge(
        @Query("mapX") mapX : Double,
        @Query("mapY") mapY : Double,
        @Query("radius") radius : Int
    ): Call<ResponseGetResultForCurrent>

    @GET("/sp/location-based-list")
    fun getResultForCurrent_sp(
        @Query("mapX") mapX : Double,
        @Query("mapY") mapY : Double,
        @Query("radius") radius : Int
    ): Call<ResponseGetResultForCurrent>

    companion object {

        fun create(): CurrentLocationAPI {
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
                .create(CurrentLocationAPI::class.java)
        }
    }
}