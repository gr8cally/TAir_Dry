package com.example.airdry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.airdry.Network.ForecastResponse
import com.example.airdry.Network.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOError

class DryTimeRecyclerViewAdapter (private val dryTimeList: List<DryTimeEntry>, private val context: Context)
    : RecyclerView.Adapter<DryTimeCardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DryTimeCardHolder {

        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.dry_time_card, parent, false)
        return DryTimeCardHolder(layoutView)
    }

    override fun getItemCount(): Int {

        return dryTimeList.size
    }

    override fun onBindViewHolder(holder: DryTimeCardHolder, position: Int) {

        if (position < dryTimeList.size) {
            val dryTime = dryTimeList[position]
            holder.dryTimeTitle.text = context.resources.getString(dryTime.title)
            holder.dryTimeImage.setImageResource(dryTime.imageResource)

        }

        holder.dryTimeImage.setOnClickListener {
            if (lat.isNullOrEmpty()){
                Toast.makeText(context, "Please select a valid Location", Toast.LENGTH_SHORT).show()
            }
            else{
                val context = holder.dryTimeImage.context
                getForecastResponse(context, position)
                lat = ""
                lon = ""
            }


        }

//        holder.button.setOnClickListener {
//            val context = holder.view.context
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra(DetailActivity.LETTER, holder.button.text.toString())
//            context.startActivity(intent)
//        }


    }
    companion object {

        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "5d60dac58f977b6e57bae62db28dc160"
        var lat = ""
        var lon = ""
        var exclude = "minutely,daily"
        var overallResponse: ForecastResponse? = null
    }

    private fun getForecastResponse(context: Context, position: Int): ForecastResponse?{
        var forecastResponse: ForecastResponse? = null
        val service = retrofitBuilder()
        val call = service.getForecastWeatherData(lat, lon, exclude, AppId)
        call.enqueue(object : Callback<ForecastResponse> {

            override fun onResponse(
                    call: Call<ForecastResponse>,
                    response: Response<ForecastResponse>
            ) {
                if (response.code() == 200) {
                    var weatherResponse = response.body()!!
                    forecastResponse = weatherResponse
                    overallResponse = weatherResponse
                    val intent = Intent(context, RecommendActivity::class.java)
                    intent.putExtra("position", position.toString())
                    //intent.putExtra("response", weatherResponse)
                    Log.d("pam", weatherResponse.current?.wind_speed.toString())
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                Log.v("retrofit", (t.message).toString())
            }
        })
        return forecastResponse
    }

    private fun retrofitBuilder():  WeatherService{
        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(WeatherService::class.java)
        return service
    }

    fun getForecast(): ForecastResponse? {
        return overallResponse
    }
}