package com.bps.gotwinter2021.ui.house

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.databinding.HouseFragmentBinding

class HouseFragment : Fragment() {
    val application = requireNotNull(activity).application

    val viewModel: HouseViewModel by lazy{
        createViewModel {
            HouseViewModel(requireActivity().application,
                GOTRepo.provideGOTRepo()
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = HouseFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModelHouse = viewModel
        //house selected at home screen
        val houseSelected = HouseFragmentArgs.fromBundle(requireArguments()).houseName

        return binding.root
    }

}
