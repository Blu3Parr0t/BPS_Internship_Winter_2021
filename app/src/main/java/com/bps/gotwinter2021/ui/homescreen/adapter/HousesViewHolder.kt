package com.bps.gotwinter2021.ui.homescreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.R

class HousesViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.houses_card_view, parent,false
    )
){
    val houseName = itemView.findViewById<TextView>(R.id.house_name_TV)
    val houseImage: ImageView = itemView.findViewById<ImageView>(R.id.house_card_IV)

    fun bind(classDataWrapper: String?){
        houseName.text = classDataWrapper
        houseImage.setImageResource(
            when(classDataWrapper) {
                "Stark" -> R.drawable.stark
                "Lannister" -> R.drawable.lannister
                "Targaryen" -> R.drawable.targaryen
                "Baratheon" -> R.drawable.baratheon
                "Greyjoy" -> R.drawable.greyjoy
                "Tully" -> R.drawable.tully
                "Arryn" -> R.drawable.arryn
                "Tyrel" -> R.drawable.tyrell
                else -> R.drawable.no_image
            }
        )
    }
}