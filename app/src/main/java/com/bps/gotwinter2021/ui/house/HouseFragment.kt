package com.bps.gotwinter2021.ui.house

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.databinding.HouseFragmentBinding
import com.bps.gotwinter2021.ui.house.adapter.HouseGridAdapter

class HouseFragment : Fragment() {

    val viewModel: HouseViewModel by lazy{
        createViewModel {
            HouseViewModel(
                requireActivity().application,
                GOTRepo.provideGOTRepo()
            )
        }
    }

    private lateinit var binding: HouseFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.house_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModelHouse = viewModel
        //house selected at home screen
        val houseSelected = HouseFragmentArgs.fromBundle(requireArguments()).houseName
        viewModel.fetchCharactersByHouse(house = "House "+ houseSelected)

        binding.houseCharacterGrid.adapter = HouseGridAdapter()

        return binding.root
    }
}
