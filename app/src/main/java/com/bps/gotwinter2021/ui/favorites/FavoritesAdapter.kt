package com.bps.gotwinter2021.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.FavoritesItemCardViewBinding
import com.bps.gotwinter2021.favorites.database.Favorite

class FavoritesAdapter(private val onClickListener: FavoritesClickListener) : ListAdapter<Favorite, FavoritesAdapter.FavoritesViewHolder>(DiffCallback){
    class FavoritesViewHolder(private var binding: FavoritesItemCardViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite, clickListener: FavoritesClickListener){
            binding.favorite = favorite
            binding.favCardItemViewNameTV.text = favorite.characterName
            binding.favCardItemViewFavoriteIV.setOnClickListener{
                clickListener.onFavoriteClick(favorite, "favorite")
            }
            binding.favCardItemViewCharacterIV.setOnClickListener{
                clickListener.onNavClick(favorite, "navigate")
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Favorite>(){
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.characterName == newItem.characterName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(FavoritesItemCardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val fav = getItem(position)
        holder.bind(fav, onClickListener)
    }
}

class FavoritesClickListener(val clickListener:(fav: Favorite, identifier: String)-> Unit){
    fun onFavoriteClick(character: Favorite, identifier: String) = clickListener(character, identifier)
    fun onNavClick(character: Favorite, identifier: String) = clickListener(character, identifier)
}
