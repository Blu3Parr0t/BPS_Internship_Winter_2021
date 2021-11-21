package com.bps.gotwinter2021.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.FavoritesFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FavoritesFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.favoritesViewModel = viewModel
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.favoritesGrid.adapter = FavoritesAdapter(FavoritesClickListener{
            addFav, identifier ->
            if(identifier == "navigate"){
                viewModel.justNav()
                viewModel.navToOverview(addFav)
            }
            else{
                viewModel.addClicked(addFav)
            }
        })

        viewModel.navigateOverview.observe(viewLifecycleOwner) {
            if (viewModel.navYet.value == true) {
                val arrayTitles: List<String> = it.characterTitle.split(",").map { it.trim() }
                val newOver = GOTResponse(
                    id = "",
                    name = it.characterName,
                    image = it.characterImage,
                    house = it.characterHouse,
                    titles = arrayTitles,
                    father = it.characterFamily,
                    mother = ""
                )
                this.findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToOverviewFragment(newOver)
                )
            }
            viewModel.doneNav()
        }
        return binding.root
    }
}
