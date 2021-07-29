package com.example.airdry

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.util.ArrayList

class DryTimeEntry (val imageResource: Int, val title: Int) {

    companion object {
        /**
         * Loads the different dry time and text into a List and returns
         */
        fun initProductEntryList(): List<DryTimeEntry> {
            val image_text_array = listOf(
                DryTimeEntry(R.drawable.drytime_1, R.string.dry_time_text1),
                DryTimeEntry(R.drawable.drytime_2, R.string.dry_time_text2),
                DryTimeEntry(R.drawable.drytime_3, R.string.dry_time_text3),
                DryTimeEntry(R.drawable.drytime_4, R.string.dry_time_text4),
                DryTimeEntry(R.drawable.drytime_5, R.string.dry_time_text4)
            )
            return image_text_array
        }
    }
}