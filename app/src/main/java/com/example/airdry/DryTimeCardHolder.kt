package com.example.airdry

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView

class DryTimeCardHolder (itemView: View) //TODO: Find and store views from itemView
    : RecyclerView.ViewHolder(itemView){
    var dryTimeImage: ImageView = itemView.findViewById(R.id.product_image)
    var dryTimeTitle: TextView = itemView.findViewById(R.id.product_title)
}