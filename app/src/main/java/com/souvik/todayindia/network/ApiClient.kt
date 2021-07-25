package com.souvik.todayindia.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.souvik.todayindia.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var retrofit: Retrofit? = null
    private var interceptor: HttpLoggingInterceptor? = null
    private var client: OkHttpClient? = null
    private var gson: Gson? = null
    private var mapiInterface: ApiInterface? = null
    private var BASE_URL_RETROFIT = ""


    fun changeBaseUrl(baseUrl : String){
        BASE_URL_RETROFIT = baseUrl
    }

    fun destroyRetrofit(){
        retrofit = null
        mapiInterface = null
    }

    val apiInterface: ApiInterface?
        get() {

            if (interceptor == null) {
                interceptor = HttpLoggingInterceptor()
                interceptor?.setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            if (client == null) {
                client = if (BuildConfig.DEBUG) {
                    OkHttpClient.Builder()
                        .addInterceptor(interceptor!!)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).build()
                } else {
                    OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).build()
                }
            }
            if (gson == null) {
                gson = GsonBuilder()
                    .setLenient()
                    .create()
            }

            if (retrofit == null) {
                Log.d("Retrofit", "Creating Instance")
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL_RETROFIT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }

            if (mapiInterface == null) {
                mapiInterface = retrofit!!.create(ApiInterface::class.java)
            }
            return mapiInterface
        }

}