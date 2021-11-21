package com.bps.gotwinter2021.ui.overview

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.OverviewFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    val viewModel: OverviewViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = OverviewFragmentBinding.inflate(inflater)
        val passedCharacter = OverviewFragmentArgs.fromBundle(requireArguments()).characterPassed
        viewModel.getPassedArg(passedCharacter)

        binding.lifecycleOwner = this
        binding.viewModelOverview = viewModel
        binding.backArrow2.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.characterFamily.text = viewModel.determineFamily(passedCharacter)
        binding.characterTitle.text = viewModel.setTitle(passedCharacter)

        val house: String = "<b> House: </b>" + passedCharacter.house
        binding.characterHouse.text = Html.fromHtml(house)

        return binding.root
    }
}
