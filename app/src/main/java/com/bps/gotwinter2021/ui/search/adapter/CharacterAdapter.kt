package com.bps.gotwinter2021.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.ItemCardViewBinding

class CharacterAdapter :
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
        character?.let { holder.bindCharacter(it) }
    }

    class CharactersByNameViewHolder(private var binding: ItemCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCharacter(character: GOTResponse) {
            binding.character = character
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
}
