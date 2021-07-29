package com.example.airdry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.widget.AutocompleteSupportFragment


class MainActivity : AppCompatActivity(), NavigationHost {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, HomepageFragment())
                .commit()
        }
        //testa()




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

//    fun testa()
//    {
//        Log.d("HPFrag", "one")
//        val client = OkHttpClient()
//        Log.d("HPFrag", "two")
//        val request = Request.Builder()
//                .url("https://community-open-weather-map.p.rapidapi.com/forecast?q=san%20francisco%2Cus")
//                .get()
//                .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
//                .build()
//        Log.d("HPFrag", "threeya")
//        val response = client.newCall(request).execute()
//        //Log.d("HPFrag", "threeyame")
//        //Log.d("HPFrag", (response.code().toString()))
//    }


}