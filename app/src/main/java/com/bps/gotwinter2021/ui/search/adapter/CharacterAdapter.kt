package com.bps.gotwinter2021.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.ItemCardViewBinding

class CharacterAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<CharacterAdapter.CharactersByNameViewHolder>() {

    private var isFavorite: Boolean? = false
    private var character: GOTResponse? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersByNameViewHolder {
        return CharactersByNameViewHolder(
            ItemCardViewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CharactersByNameViewHolder, position: Int) {
        character?.let { holder.bindCharacter(it, onClickListener, position, isFavorite) }
    }

    class CharactersByNameViewHolder(private var binding: ItemCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCharacter(
            character: GOTResponse,
            clickListener: OnClickListener,
            position: Int,
            isFavorite: Boolean?
        ) {
            binding.character = character
            binding.cardItemViewFavoriteIV.setOnClickListener {
                clickListener.onClick(character, "favorite")
            }
            binding.cardItemViewCharacterIV.setOnClickListener {
                clickListener.onFavoriteCLick(character, "navigate")
            }
            if (isFavorite == true) binding.cardItemViewFavoriteIV.setImageResource(R.drawable.ic_basic_heart_fill)
            else binding.cardItemViewFavoriteIV.setImageResource(R.drawable.ic_basic_heart_outline)
            binding.executePendingBindings()
        }
    }

    fun setData(character: GOTResponse?, favorite: Boolean?) {
        if (character != null) {
            this.character = character
            isFavorite = favorite
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        if (character != null) {
            size = 1
        }
        return size
    }

    fun updateItem(favorite: Boolean) {
        isFavorite = favorite
        notifyDataSetChanged()
    }

    class OnClickListener(
        val clickListener: (
            character: GOTResponse, identifier: String
        ) -> Unit
    ) {
        fun onClick(character: GOTResponse, identifier: String) =
            clickListener(character, identifier)

        fun onFavoriteCLick(character: GOTResponse, identifier: String) =
            clickListener(character, identifier)
    }
}
