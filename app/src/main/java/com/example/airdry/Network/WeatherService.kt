package com.example.airdry.Network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon")
    lon: String, @Query("APPID") app_id: String): Call<WeatherResponse>

    @GET("data/2.5/onecall?")
    fun getForecastWeatherData(@Query("lat") lat: String, @Query("lon")
    lon: String,@Query("exclude") exclude: String, @Query("APPID") app_id: String): Call<ForecastResponse>

    //https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
    //api.openweathermap.org/data/2.5/weather?q={city name},{state code}&appid={API key}

}