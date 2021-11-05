package com.bps.gotwinter2021

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.ui.house.adapter.HouseGridAdapter

@BindingAdapter("houseCharacters")
fun bindHouseRecyclerView(recyclerView: RecyclerView, data: List<GOTResponse>?){
    val adapter = recyclerView.adapter as HouseGridAdapter
    adapter.submitList(data)
}
