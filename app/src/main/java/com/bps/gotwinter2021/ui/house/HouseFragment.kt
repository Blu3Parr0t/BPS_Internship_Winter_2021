package com.bps.gotwinter2021.ui.house

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.databinding.HouseFragmentBinding
import com.bps.gotwinter2021.ui.house.adapter.HouseGridAdapter
import com.bps.gotwinter2021.ui.house.adapter.HousesFragmentOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HouseFragment : Fragment() {

    val viewModel: HouseViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = HouseFragmentBinding.inflate(inflater)
        val houseSelected = HouseFragmentArgs.fromBundle(requireArguments()).houseName

        binding.lifecycleOwner = this
        binding.viewModelHouse = viewModel
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.fetchCharactersByHouse(house = "House "+ houseSelected)
        binding.houseFragmentTitle.text = houseSelected
        binding.houseFragmentLayout.setBackgroundResource(viewModel.setBackground(houseSelected))

        binding.houseFragmentCharacterGrid.adapter = HouseGridAdapter(HousesFragmentOnClickListener { addCharacter, identifier ->
            if(identifier == "navigate"){
                viewModel.justNav()
                viewModel.navToOverview(addCharacter)
            }else {
                viewModel.addClicked(addCharacter)
            }
        })

        viewModel.navigateOverview.observe(viewLifecycleOwner) {
            if (viewModel.navYet.value == true) {
                this.findNavController()
                    .navigate(HouseFragmentDirections.actionHouseFragmentToOverviewFragment(it))
            }
            viewModel.doneNav()
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                HouseViewModel.GOTApiStatus.LOADING -> {
                    binding.houseFragmentCharacterGrid.visibility = View.GONE
                    binding.searchResultsLoadingHouse.visibility = View.VISIBLE
                }
                HouseViewModel.GOTApiStatus.DONE -> {
                    binding.houseFragmentCharacterGrid.visibility = View.VISIBLE
                    binding.searchResultsLoadingHouse.visibility = View.GONE
                }
                else -> {
                }
            }
        }
        return binding.root
    }
}
