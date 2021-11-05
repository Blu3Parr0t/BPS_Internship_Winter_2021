package com.bps.gotwinter2021.ui.house.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.CardItemViewBinding

class HouseGridAdapter: ListAdapter<GOTResponse, HouseGridAdapter.CharacterByHouseViewHolder>(DiffCallback){
    companion object DiffCallback : DiffUtil.ItemCallback<GOTResponse>() {
        override fun areItemsTheSame(oldItem: GOTResponse, newItem: GOTResponse): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GOTResponse, newItem: GOTResponse): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class CharacterByHouseViewHolder(private var binding: CardItemViewBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(character: GOTResponse){
            binding.character = character
            binding.cardItemViewNameTV.text = character.name
            //binding.cardItemViewCharacterIV = character.image
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterByHouseViewHolder {
        return CharacterByHouseViewHolder(CardItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CharacterByHouseViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }
}
