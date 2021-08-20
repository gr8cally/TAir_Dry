package com.example.airdry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airdry.databinding.ActivityMainBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class MainActivity : AppCompatActivity(), NavigationHost {
    private lateinit var latLng: LatLng;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1,
                RecyclerView.VERTICAL, false)
        val adapter = DryTimeRecyclerViewAdapter(
                DryTimeEntry.initProductEntryList(), this as AppCompatActivity)
        recyclerView.adapter = adapter
        placesFind()



    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    fun placesFind() {
        //Initialize the SDK
        if (! Places.isInitialized()){
            Places.initialize(applicationContext, "AIzaSyC36hWIXMtfwt8pZJUL25wlB2mCNLBeEUk")}
        //Create a new places client instance
        val placesClient: PlacesClient = Places.createClient(this)

        //initialize the AutocompleteSupportFragment
        val autocompleteFragment =
                supportFragmentManager.findFragmentById(R.id.autocomplete_fragment1)
                        as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: ${place.name}, ${place.id}, ${place.latLng}")
                latLng = place.latLng!!
                DryTimeRecyclerViewAdapter.lat = latLng.latitude.toString()
                DryTimeRecyclerViewAdapter.lon = latLng.longitude.toString()
            }

            override fun onError(p0: Status) {
                Log.i("TAG", "An error occurred: ${p0}")
            }
        })
    }


}