package com.bps.gotwinter2021.ui.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.databinding.OverviewFragmentBinding
import com.bps.gotwinter2021.favorites.database.FavoriteDatabase

class OverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = OverviewFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val passedCharacter = OverviewFragmentArgs.fromBundle(requireArguments()).characterPassed
        val application = requireNotNull(activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: OverviewViewModel by lazy{
            createViewModel {
                OverviewViewModel(requireActivity().application,
                    dataSource, passedCharacter
                )
            }
        }
        binding.viewModelOverview = viewModel

        return binding.root
    }
}
