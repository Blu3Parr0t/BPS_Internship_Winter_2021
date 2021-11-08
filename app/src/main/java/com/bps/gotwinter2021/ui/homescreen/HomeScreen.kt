package com.bps.gotwinter2021.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.databinding.FragmentHomeScreenBinding
import com.bps.gotwinter2021.ui.homescreen.adapter.HousesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreen : Fragment() {
    private val viewModel: HomeScreenViewModel by viewModels()
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var adapter: HousesAdapter
    private val houses: List<String> = listOf(
        "Stark",
        "Lannister",
        "Targaryen",
        "Baratheon",
        "Greyjoy",
        "Martell",
        "Tully",
        "Arryn",
        "Tyrel"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = HousesAdapter(HousesAdapter.OnClickListener {
            it?.let {
                this.findNavController()
                    .navigate(HomeScreenDirections.actionHomeScreenFragmentToHouseFragment(it))
            }
        })

        adapter.setData(houses)
        binding.homeScreenRV.adapter = adapter

        viewModel.navigateToSearchResults.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(
                    HomeScreenDirections.actionHomeScreenFragmentToSearchResultsFragment(
                        viewModel.searchText.value!!
                    )
                )
                viewModel.doneNavigation()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.diTestFunction()
    }
}
