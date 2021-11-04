package com.bps.gotwinter2021.ui.overview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.databinding.OverviewFragmentBinding
import com.bps.gotwinter2021.favorites.database.FavoriteDatabase

class OverviewFragment : Fragment() {

    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = OverviewFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        //argument passed
        val characterNamePassed = ""
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao
        val viewModelFactory = OverviewViewModelFactory(characterNamePassed, application, dataSource)
        binding.viewModelOverview = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)

        return binding.root
    }
}
