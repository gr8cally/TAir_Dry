package com.example.airdry

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.airdry.Network.ForecastResponse
import com.example.airdry.Network.WeatherResponse
import com.example.airdry.Network.WeatherService
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TesterActivity : AppCompatActivity() {


    private lateinit var latLng: LatLng;
    var statusTexta = false
    var goodHours = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tester)



        //Initialize the SDK
        if (! Places.isInitialized()){
        Places.initialize(applicationContext, "AIzaSyC36hWIXMtfwt8pZJUL25wlB2mCNLBeEUk")}
        //Create a new places client instance
        val placesClient: PlacesClient = Places.createClient(this)

        //initialize the AutocompleteSupportFragment
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.

                Log.i("TAG", "Place: ${place.name}, ${place.id}, ${place.latLng}")
                latLng = place.latLng!!
                lat = latLng.latitude.toString()
                lon = latLng.longitude.toString()
                val textView = findViewById<TextView>(R.id.textview_first)
                val textView2 = findViewById<TextView>(R.id.textview_second)
                val textView3 = findViewById<TextView>(R.id.textview_third)
                val textView4 = findViewById<TextView>(R.id.textview_fourth)
                retrobut(textView)
                //Log.d("buga", "entered01")
                statusTexta = false
                goodHours = mutableListOf<Int>()
                hourLoop(textView2, textView3, textView4,0, 24)
                
                Log.d("bugati", goodHours.toString())

            }

            override fun onError(p0: Status) {
                Log.i("TAG", "An error occurred: $p0")
            }


        })





    }

    fun retrobut(textView: TextView){


        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)

        call.enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!
                    Log.d("heur", weatherResponse.weather.size.toString() )

                    val stringBuilder = "Country: " +
                            weatherResponse.sys!!.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main!!.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main!!.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main!!.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main!!.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main!!.pressure

                    textView!!.text = stringBuilder
                    Log.d("darey", stringBuilder)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.v("retrofit", (t.message).toString())
                textView!!.text = t.message
            }
        })
    }
    companion object {

        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "5d60dac58f977b6e57bae62db28dc160"
        var lat = "35"
        var lon = "139"
        var exclude = "minutely,daily"
    }

    fun retrobut2(textView: TextView, textView1: TextView, textView2: TextView){


        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getForecastWeatherData(lat, lon, exclude, AppId)

        call.enqueue(object : Callback<ForecastResponse> {

            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder ="Current Hour" +
                            "\n"+
                            "Temperature: " +
                            weatherResponse.current!!.temp +
                            "\n" +
                            "WindSpeed: " +
                            weatherResponse.current!!.wind_speed +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.current!!.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.current!!.pressure
//
                    textView!!.text = stringBuilder
//                    Log.d("darey", stringBuilder)
                    val list = listOf<TextView>(textView1, textView2)
                    for (i in 1..2) {
                        val stringBuilder ="Hour ${i + 1}" +
                                "\n"+
                                "Temperature: " +
                                weatherResponse.hourly[i]!!.temp +
                                "\n" +
                                "WindSpeed: " +
                                weatherResponse.hourly[i]!!.wind_speed +
                                "\n" +
                                "Humidity: " +
                                weatherResponse.hourly[i]!!.humidity +
                                "\n" +
                                "Pressure: " +
                                weatherResponse.hourly[i]!!.pressure
//
                        list[i-1]!!.text = stringBuilder
                    }
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                Log.v("retrofit", (t.message).toString())
                textView!!.text = t.message
            }
        })
    }

    fun retrobut3(textView: TextView, textView1: TextView, textView2: TextView, start: Int, end: Int){


        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getForecastWeatherData(lat, lon, exclude, AppId)


        call.enqueue(object : Callback<ForecastResponse> {

            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {
                if (response.code() == 200) {


                    val weatherResponse = response.body()!!
                    val list = listOf(textView, textView1, textView2)
                    val threeDaysMap = mutableMapOf<Int, MutableList<Any>>()
                    var step = 0
                    for (i in start until end) {
                        Log.d("buga", " " + "${start} - ${end} - ${i}")
                        val threeDaysList = mutableListOf<Any>()
                        val hour = weatherResponse.hourly[i]!!
                        val stringBuilder ="Hour ${i + 1}" +
                                "\n"+
                                "Temperature: " +
                                hour.temp +
                                "\n" +
                                "WindSpeed: " +
                                hour.wind_speed +
                                "\n" +
                                "Humidity: " +
                                hour.humidity +
                                "\n" +
                                "Pressure: " +
                                hour.pressure
                        threeDaysList.add(hour.weatherForecast[0].id)
                        threeDaysList.add(hour.wind_speed)
                        threeDaysList.add(hour.humidity)
                        threeDaysMap[step] = threeDaysList

                        step++

                        //list[i]!!.text = stringBuilder
                        //Log.d("buga", "" +weatherResponse.lat + " la - lon " + weatherResponse.lon)
                    }
                    Log.d("buga", " " + threeDaysMap.values)
                    val temp = recomendation(threeDaysMap)
                    updateRecom(textView, textView1, textView2, temp)


                    if (temp.equals("pp")){
                        goodHours.add(start)
                        statusTexta = true
                        }

//                    Handler(Looper.getMainLooper()).postDelayed({
//                        // Your Code
//                        val temp = recomendation(threeDaysMap)
//                        updateRecom(textView, textView1, textView2, temp)
//
//                        if (temp.equals("pp")){
//
//                            statusTexta = true
//                        }
//                        //Log.d("buga", temp + " tmp")
//                    }, 1000)

                    //Log.d("buga", threeDaysMap.toString())
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                Log.v("retrofit", (t.message).toString())
                textView!!.text = t.message
            }
        })
    }

    fun recomendation(responseMap: MutableMap<Int, MutableList<Any>>): String{
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
                return returnText
            }
            else if (j[1] as Float !in acceptableWindRange){
                returnText = "While there will be no rain,\n it will be too windy to Airdry"
                //Log.d("buga", returnText)
                return returnText
            }
            else if (!(j[2] as Float in acceptableHumidityRange )){
                returnText = "While there will be no rain,\n its too humid, drying would take so long"
                //Log.d("buga", returnText)
                return returnText
            }
            else if (j[0] in midIdRange){
                returnText = "special case"
                //Log.d("buga", returnText)
                return returnText
            }

        }
        //returnText = "Go and wash in peace,\n conditions are good"
        returnText = "pp"
        return returnText
        //Log.d("buga", "Go and wash in peace,\n conditions are good")

    }

    fun hourLoop( textView: TextView, textView1: TextView, textView2: TextView, start: Int, end: Int){

        if (end - start <=3){
            retrobut3(textView, textView1, textView2, start, end)
        }

        else{
            for (i in start until end){
                retrobut3(textView, textView1, textView2, i, i+3)
                //Log.d("buga", "${i} ${statusTexta} hour")
                if (statusTexta){
                    Log.d("buga", "cookin in ${i} hour")
                    return
                }
            }
            Log.d("buga", "we didnt find shii guys")

        }
    }

    fun updateRecom (textView: TextView, textView1: TextView, textView2: TextView, updateText: String){
        textView1.text = ""
        textView2.text = ""
        textView.text = updateText
    }
}