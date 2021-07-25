package com.souvik.todayindia.network

import com.google.gson.JsonObject
import com.souvik.todayindia.model.Article
import com.souvik.todayindia.model.Category
import com.souvik.todayindia.model.WeatherData
import com.souvik.todayindia.utility.weatherApiKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("category") category: String? = null) : Response<Article>

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("APPID") app_id: String = weatherApiKey
    ): Response<WeatherData>

}