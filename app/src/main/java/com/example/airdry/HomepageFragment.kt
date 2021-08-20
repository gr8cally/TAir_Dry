package com.example.airdry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airdry.Network.WeatherResponse
import com.example.airdry.Network.WeatherService
import com.example.airdry.Overview.OverviewViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.homepage_fragment.*
import kotlinx.android.synthetic.main.homepage_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomepageFragment : Fragment() {




    private var weatherData: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val viewModel: OverviewViewModel by viewModels()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment with the ProductGrid theme
        val view = inflater.inflate(R.layout.homepage_fragment, container, false)

        // Set up the toolbar.
        //(activity as AppCompatActivity).setSupportActionBar(view.app_bar)
        view.recycler_view.setHasFixedSize(true)

        view.recycler_view.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)
        val adapter = DryTimeRecyclerViewAdapter(
            DryTimeEntry.initProductEntryList(), activity as AppCompatActivity)
        view.recycler_view.adapter = adapter
        Log.d("uku", (parentFragmentManager.findFragmentById(R.id.autocomplete_fragment1)== null).toString() )
        //Log.d("uku1", (supportFragmentManager.findFragmentById(R.id.fragment_container)))
        parentFragmentManager.findFragmentById(R.id.autocomplete_fragment1)
        //view.textViewsr.text = "Long"
        //retrobut(view.textViewsr)
        //placesFind()


//        val largePadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing)
//        val smallPadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small)
//        view.recycler_view.addItemDecoration(ProductGridItemDecoration(largePadding, smallPadding))




        return view
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
    }

    fun placesFind() {
        //Initialize the SDK
        if (! Places.isInitialized()){
            Places.initialize(requireActivity().applicationContext, "AIzaSyC36hWIXMtfwt8pZJUL25wlB2mCNLBeEUk")}
        //Create a new places client instance
        val placesClient: PlacesClient = Places.createClient(requireActivity().applicationContext)

        //initialize the AutocompleteSupportFragment
        val manager: FragmentManager = (context as MainActivity).getSupportFragmentManager()
        val autocompleteFragment =

                parentFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                        as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: ${place.name}, ${place.id}, ${place.latLng}")
            }

            override fun onError(p0: Status) {
                Log.i("TAG", "An error occurred: ${p0}")
            }
        })
    }

}