package com.bps.gotwinter2021.ui.homescreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HousesAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<HousesViewHolder>() {
    private var houses: List<String?> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HousesViewHolder {
        return HousesViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: HousesViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onClickListener.onClick(houses[position])
        }
        holder.bind(houses[position])
    }

    override fun getItemCount(): Int {
        return houses.size
    }

    fun setData(housesList: List<String?>){
        this.houses = housesList
        notifyDataSetChanged()
    }

    class OnClickListener(val clickListener: (nameCLass: String?) -> Unit) {
        fun onClick(nameCLass: String?) = clickListener(nameCLass)
    }
}
