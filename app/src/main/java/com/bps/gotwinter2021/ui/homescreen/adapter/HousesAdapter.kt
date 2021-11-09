package com.bps.gotwinter2021.ui.homescreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.databinding.HousesCardViewBinding
import com.bps.gotwinter2021.databinding.ItemCardViewBinding

class HousesAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<HousesAdapter.HousesViewHolder>() {
    private var houses: List<String> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HousesViewHolder {
        return HousesViewHolder(
            HousesCardViewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: HousesViewHolder, position: Int) {
        holder.bind(houses[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return houses.size
    }

    fun setData(housesList: List<String>) {
        this.houses = housesList
        notifyDataSetChanged()
    }

    class OnClickListener(val clickListener: (nameCLass: String) -> Unit) {
        fun onClick(nameCLass: String) = clickListener(nameCLass)
    }

    class HousesViewHolder(private var binding: HousesCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataWrapper: String, clickLister: HousesAdapter.OnClickListener) {
            binding.houseNameTV.text = dataWrapper
            binding.houseCardIV.setImageResource(
                when (dataWrapper) {
                    "Stark" -> R.drawable.stark
                    "Lannister" -> R.drawable.lannister
                    "Targaryen" -> R.drawable.targaryen
                    "Baratheon" -> R.drawable.baratheon
                    "Greyjoy" -> R.drawable.greyjoy
                    "Tully" -> R.drawable.tully
                    "Arryn" -> R.drawable.arryn
                    "Tyrell" -> R.drawable.tyrell
                    else -> R.drawable.no_image
                }
            )
            binding.houseCardLL.setOnClickListener {
                clickLister.onClick(dataWrapper)
            }
        }
    }
}
