package com.bps.gotwinter2021.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.ItemCardViewBinding
import com.bps.gotwinter2021.ui.homescreen.adapter.HousesAdapter

class CharacterAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<CharacterAdapter.CharactersByNameViewHolder>() {

    private var character: GOTResponse? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersByNameViewHolder {
        return CharactersByNameViewHolder(
            ItemCardViewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CharactersByNameViewHolder, position: Int) {
        character?.let { holder.bindCharacter(it, onClickListener) }
    }

    class CharactersByNameViewHolder(private var binding: ItemCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCharacter(character: GOTResponse, clickListener: OnClickListener) {
            binding.character = character
            binding.cardItemViewFavoriteIV.setOnClickListener{
                clickListener.onClick(character, "favorite")
            }
            binding.cardItemViewCharacterIV.setOnClickListener{
                clickListener.onFavoriteCLick(character, "navigate")
            }
            binding.executePendingBindings()
        }
    }

    fun setData(character: GOTResponse?){
        if (character != null) {
            this.character = character
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        if (character != null){
            size = 1
        }
        return size
    }

    class OnClickListener(val clickListener: (character: GOTResponse, identifier : String) -> Unit) {
        fun onClick(character: GOTResponse, identifier : String) = clickListener(character, identifier )
        fun onFavoriteCLick(character: GOTResponse, identifier : String) = clickListener(character, identifier )
    }
}
