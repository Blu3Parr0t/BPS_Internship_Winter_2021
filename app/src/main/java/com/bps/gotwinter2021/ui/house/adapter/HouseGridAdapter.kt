package com.bps.gotwinter2021.ui.house.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.ItemCardViewBinding

class HouseGridAdapter(private val onClickListener: HousesFragmentOnClickListener): ListAdapter<GOTResponse, HouseGridAdapter.CharacterByHouseViewHolder>(DiffCallback){
    companion object DiffCallback : DiffUtil.ItemCallback<GOTResponse>() {
        override fun areItemsTheSame(oldItem: GOTResponse, newItem: GOTResponse): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GOTResponse, newItem: GOTResponse): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class CharacterByHouseViewHolder(private var binding: ItemCardViewBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(character: GOTResponse, clickListener: HousesFragmentOnClickListener){
            binding.character = character
            binding.cardItemViewNameTV.text = character.name
            binding.cardItemViewFavoriteIV.setOnClickListener{
                clickListener.onFavoriteClick(character, "favorite")
            }
            binding.cardItemViewCharacterIV.setOnClickListener{
                clickListener.toOverviewClick(character, "navigate")
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterByHouseViewHolder {
        return CharacterByHouseViewHolder(ItemCardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CharacterByHouseViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character, onClickListener)
    }
}

class HousesFragmentOnClickListener(val clickListener: (character: GOTResponse, identifier: String) -> Unit){
    fun onFavoriteClick(character: GOTResponse, identifier: String) = clickListener(character, identifier)
    fun toOverviewClick(character: GOTResponse, identifier: String) = clickListener(character, identifier)
}
