package com.prac.cabstone.search_result

import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.models.ResponseGetAreaCode
import com.prac.cabstone.models.ResponseGetInfoForArea
import com.prac.cabstone.search.GetAreaCodeAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface GetInfoForAreaCodeAPI {
    @GET("/ko/area-based-list/{areacode}")
    fun getResultForAreaCode(@Path("areacode") areacode: Int): Call<ResponseGetInfoForArea>

    @GET("/en/area-based-list/{areacode}")
    fun getResultForAreaCode_en(@Path("areacode") areacode: Int): Call<ResponseGetInfoForArea>

    @GET("/jp/area-based-list/{areacode}")
    fun getResultForAreaCode_jp(@Path("areacode") areacode: Int): Call<ResponseGetInfoForArea>

    @GET("/ch/area-based-list/{areacode}")
    fun getResultForAreaCode_ch(@Path("areacode") areacode: Int): Call<ResponseGetInfoForArea>

    @GET("/ge/area-based-list/{areacode}")
    fun getResultForAreaCode_ge(@Path("areacode") areacode: Int): Call<ResponseGetInfoForArea>

    @GET("/sp/area-based-list/{areacode}")
    fun getResultForAreaCode_sp(@Path("areacode") areacode: Int): Call<ResponseGetInfoForArea>

    companion object {

        fun create(): GetInfoForAreaCodeAPI {
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
                .create(GetInfoForAreaCodeAPI::class.java)
        }
    }
}