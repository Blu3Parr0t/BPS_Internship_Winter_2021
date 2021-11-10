package com.bps.gotwinter2021

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.ui.favorites.FavoritesAdapter
import com.bps.gotwinter2021.ui.house.adapter.HouseGridAdapter
@BindingAdapter("favoriteCharacters")
fun bindFavoritesRecyclerView(recyclerView: RecyclerView, data: List<Favorite>?){
    val adapter = recyclerView.adapter as FavoritesAdapter
    adapter.submitList(data)
}
@BindingAdapter("houseCharacters")
fun bindHouseRecyclerView(recyclerView: RecyclerView, data: List<GOTResponse>?){
    val adapter = recyclerView.adapter as HouseGridAdapter
    adapter.submitList(data)
}
