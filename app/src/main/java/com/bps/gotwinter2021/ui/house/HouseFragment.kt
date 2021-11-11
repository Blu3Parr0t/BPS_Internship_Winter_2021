package com.bps.gotwinter2021.ui.house

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.databinding.HouseFragmentBinding
import com.bps.gotwinter2021.favorites.database.FavoriteDatabase
import com.bps.gotwinter2021.ui.house.adapter.HouseGridAdapter
import com.bps.gotwinter2021.ui.house.adapter.HousesFragmentOnClickListener

class HouseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = HouseFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: HouseViewModel by lazy{
            createViewModel {
                HouseViewModel(
                    requireActivity().application,
                    GOTRepo.provideGOTRepo(),
                    dataSource
                )
            }
        }
        binding.lifecycleOwner = this
        binding.viewModelHouse = viewModel
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        val houseSelected = HouseFragmentArgs.fromBundle(requireArguments()).houseName
        viewModel.fetchCharactersByHouse(house = "House "+ houseSelected)

        binding.houseFragmentTitle.text = houseSelected
        setBackground(houseSelected, binding)
        binding.houseFragmentCharacterGrid.adapter = HouseGridAdapter(HousesFragmentOnClickListener { addCharacter, identifier ->
            if(identifier == "navigate"){
                viewModel.navToOverview(addCharacter)
            }else {
                viewModel.addClicked(addCharacter)
            }
        })

        viewModel.navigateOverview.observe(viewLifecycleOwner, Observer{
            this.findNavController().navigate(HouseFragmentDirections.actionHouseFragmentToOverviewFragment(it))
        })

        viewModel.status.observe(viewLifecycleOwner,{
            when(it){
                HouseViewModel.GOTApiStatus.LOADING->{
                    binding.houseFragmentCharacterGrid.visibility = View.GONE
                    binding.searchResultsLoadingHouse.visibility = View.VISIBLE
                }
                HouseViewModel.GOTApiStatus.DONE->{
                    binding.houseFragmentCharacterGrid.visibility = View.VISIBLE
                    binding.searchResultsLoadingHouse.visibility = View.GONE
                }
                else ->{}
            }
        })
        return binding.root
    }

    private fun setBackground(houseSelected: String, binding: HouseFragmentBinding) {
        when(houseSelected){
            "Lannister"-> binding.houseFragmentLayout.setBackgroundResource(R.drawable.background_house_lannister_blur)
            "Targaryen"-> binding.houseFragmentLayout.setBackgroundResource(R.drawable.background_targaryen_blur)
            "Baratheon"-> binding.houseFragmentLayout.setBackgroundResource(R.drawable.background_baratheon_blur)
            else -> binding.houseFragmentLayout.setBackgroundResource(R.drawable.blurred_fav_bg)
        }
    }
}
