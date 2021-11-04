package com.bps.gotwinter2021.ui.homescreen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HousesAdapter: RecyclerView.Adapter<HousesViewHolder>() {
    private var houses: List<String?> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HousesViewHolder {
        return HousesViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: HousesViewHolder, position: Int) {
        holder.bind(houses[position])
    }

    override fun getItemCount(): Int {
        return houses.size ?: 0
    }

    fun setData(housesList: List<String?>){
        this.houses = housesList
        notifyDataSetChanged()
    }
}