package com.example.airdry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.airdry.Network.ForecastResponse
import com.example.airdry.databinding.ActivityRecommendBinding

class RecommendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pos = intent?.extras?.getString("position").toString()
        Log.d("pam1", intent?.getParcelableExtra<ForecastResponse>("response").toString())
        Log.d("pam1", DryTimeRecyclerViewAdapter.overallResponse?.current?.wind_speed.toString())
        //Log.d("pam1", pos)
        val forecastData: ForecastResponse? = DryTimeRecyclerViewAdapter.overallResponse
        binding.recomendation.text = pos
        if (pos != null) {
            if (pos.toInt() == 0){
                val map = forecastData?.let { getThreeHours(0, 3, it) }
                if (map?.let { recomendation(it) }!!){
                    binding.recomendation.text = "Weather is good"
                }
                else{
                    binding.recomendation.text =  "weather is bad"
                }
            }

            else if (pos.toInt() == 1){
                var begin = 0
                while (begin < 24){
                    val map = forecastData?.let { getThreeHours(begin, begin+3, it) }
                    if (map?.let { recomendation(it) }!!){
                        binding.recomendation.text = "Weather is good in ${begin} Hours"
                        break
                    }
                    begin +=3
                }
            }


        }
    }

    fun getThreeHours(start: Int, stop: Int, response: ForecastResponse): MutableMap<Int, MutableList<Any>> {
        val threeHoursMap = mutableMapOf<Int, MutableList<Any>>()
        var step = 0
        for (i in start until stop) {
            Log.d("buga", " " + "${start} - ${stop} - ${i}")
            val threeHoursList = mutableListOf<Any>()
            val hour = response.hourly[i]!!
            threeHoursList.add(hour.weatherForecast[0].id)
            threeHoursList.add(hour.wind_speed)
            threeHoursList.add(hour.humidity)
            threeHoursMap[step] = threeHoursList
            step++
        }
        return threeHoursMap
    }

    fun recomendation(responseMap: MutableMap<Int, MutableList<Any>>): Boolean{

        val acceptableIdRange = 800..804
        val unAcceptableIdRange = 200..622
        val acceptableWindRange = 0.0..20.0
        val acceptableHumidityRange = 0.0..90.0
        val midIdRange = 701..781
        var returnText = "Error"


        for ((k, j) in responseMap){
            //Log.d("buga", j.toString())
            if (j[0] in unAcceptableIdRange){
                returnText = "The next three hours are not ideal for Airdrying\n There shall be Rain"
                //Log.d("buga", returnText)
                return false
            }
            else if (j[1] as Float !in acceptableWindRange){
                returnText = "While there will be no rain,\n it will be too windy to Airdry"
                //Log.d("buga", returnText)
                return false
            }
            else if (!(j[2] as Float in acceptableHumidityRange )){
                returnText = "While there will be no rain,\n its too humid, drying would take so long"
                //Log.d("buga", returnText)
                return false
            }
            else if (j[0] in midIdRange){
                returnText = "special case"
                //Log.d("buga", returnText)
                return false
            }

        }
        //returnText = "Go and wash in peace,\n conditions are good"
        returnText = "pp"
        return true
        //Log.d("buga", "Go and wash in peace,\n conditions are good")

    }
}