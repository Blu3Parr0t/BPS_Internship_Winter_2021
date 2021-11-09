package com.bps.gotwinter2021.ui.house

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
        val houseSelected = HouseFragmentArgs.fromBundle(requireArguments()).houseName
        viewModel.fetchCharactersByHouse(house = "House "+ houseSelected)

        binding.houseFragmentTitle.text = houseSelected
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

        return binding.root
    }
}
