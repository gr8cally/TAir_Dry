package com.example.airdry.Network

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ForecastResponse () : Parcelable {
    @SerializedName("hourly")
    var hourly = ArrayList<Hourly>()
    @SerializedName("current")
    var current: Current? = null
    @SerializedName("lat")
    var lat: Float = 0.toFloat()
    @SerializedName("lon")
    var lon: Float = 0.toFloat()

    constructor(parcel: Parcel) : this() {
        lat = parcel.readFloat()
        lon = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(lat)
        parcel.writeFloat(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForecastResponse> {
        override fun createFromParcel(parcel: Parcel): ForecastResponse {
            return ForecastResponse(parcel)
        }

        override fun newArray(size: Int): Array<ForecastResponse?> {
            return arrayOfNulls(size)
        }
    }

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