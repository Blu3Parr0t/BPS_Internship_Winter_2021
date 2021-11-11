package com.bps.gotwinter2021.ui.overview

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.OverviewFragmentBinding
import com.bps.gotwinter2021.favorites.database.FavoriteDatabase

class OverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = OverviewFragmentBinding.inflate(inflater)
        val passedCharacter = OverviewFragmentArgs.fromBundle(requireArguments()).characterPassed
        val application = requireNotNull(activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: OverviewViewModel by lazy{
            createViewModel {
                OverviewViewModel(requireActivity().application,
                    dataSource,
                    passedCharacter
                )
            }
        }

        binding.lifecycleOwner = this
        binding.viewModelOverview = viewModel
        binding.backArrow2.setOnClickListener {
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
        determineFamily(passedCharacter, binding)
        setInfo(passedCharacter, binding)

        return binding.root
    }

    private fun setInfo(passedCharacter: GOTResponse, binding: OverviewFragmentBinding) {
        if(passedCharacter.titles.size > 0) {
            if(passedCharacter.titles[0] != " "){
                binding.characterTitle.text = " "
            }else {
                val title: String = "<b> Title: </b>" + passedCharacter.titles[0]
                binding.characterTitle.text = Html.fromHtml(title)
            }
        }
        val house: String = "<b> House: </b>" + passedCharacter.house
        binding.characterHouse.text = Html.fromHtml(house)
    }

    private fun determineFamily(passedCharacter: GOTResponse, binding: OverviewFragmentBinding) {
        if(passedCharacter.father.isNullOrEmpty() && passedCharacter.mother.isNullOrEmpty()) {
            binding.characterFamily.text = " "
        }
        else if(passedCharacter.father.isNullOrEmpty() && !passedCharacter.mother.isNullOrEmpty() && passedCharacter.mother != " "){
            val justM: String = "<b> Family: </b>" + passedCharacter.mother
            binding.characterFamily.text = Html.fromHtml(justM)
        }
        else if(!passedCharacter.father.isNullOrEmpty() && passedCharacter.mother.isNullOrEmpty() && passedCharacter.father != " "){
            val justF: String = "<b> Family: </b>" + passedCharacter.father
            binding.characterFamily.text = Html.fromHtml(justF)
        }
        else if(passedCharacter.father == " " || passedCharacter.mother == " "){
            binding.characterFamily.text = " "
        }
        else{
            val family: String = "<b> Family: </b>" + passedCharacter.father +", " + passedCharacter.mother
            binding.characterFamily.text = Html.fromHtml(family)
        }
    }
}
