package com.bps.gotwinter2021.ui.homescreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.databinding.FragmentHomeScreenBinding
import com.bps.gotwinter2021.ui.homescreen.adapter.HousesAdapter

class HomeScreen : Fragment() {
    private val viewModel: HomeScreenViewModel by lazy {
        createViewModel { HomeScreenViewModel(app = this.requireActivity().application) }
    }
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var adapter: HousesAdapter
    private val houses: List<String> = listOf("Stark","Lannister","Targaryen","Baratheon","Greyjoy","Martell","Tully","Arryn","Tyrel")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_screen, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = HousesAdapter()

        adapter.setData(houses)
        binding.homeScreenRV.adapter = adapter

        return binding.root
    }

}
