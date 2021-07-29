package com.example.airdry.Network

import com.google.gson.annotations.SerializedName

class ForecastResponse {
    @SerializedName("hourly")
    var hourly = ArrayList<Hourly>()
    @SerializedName("current")
    var current: Current? = null
    @SerializedName("lat")
    var lat: Float = 0.toFloat()
    @SerializedName("lon")
    var lon: Float = 0.toFloat()
}

class Hourly {
    @SerializedName("dt")
    var dt: Int = 0
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("wind_speed")
    var wind_speed: Float = 0.toFloat()
    @SerializedName("weather")
    var weatherForecast = ArrayList<WeatherForecast>()
}

class Current {
    @SerializedName("dt")
    var dt: Int = 0
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("wind_speed")
    var wind_speed: Float = 0.toFloat()
}

class WeatherForecast {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("main")
    var main: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("icon")
    var icon: String? = null
}