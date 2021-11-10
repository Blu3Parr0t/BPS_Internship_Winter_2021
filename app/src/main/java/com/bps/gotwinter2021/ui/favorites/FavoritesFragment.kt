package com.bps.gotwinter2021.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.FavoritesFragmentBinding
import com.bps.gotwinter2021.favorites.database.FavoriteDatabase

class FavoritesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FavoritesFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: FavoritesViewModel by lazy{
            createViewModel {
                FavoritesViewModel(
                    requireActivity().application,
                    dataSource
                )
            }
        }
        binding.lifecycleOwner = this
        binding.favoritesViewModel = viewModel
        binding.favoritesGrid.adapter = FavoritesAdapter(FavoritesClickListener{
            addFav, identifier ->
            if(identifier == "navigate"){
                viewModel.navToOverview(addFav)
            }
            else{
                viewModel.addClicked(addFav)
            }
        })

        viewModel.navigateOverview.observe(viewLifecycleOwner, Observer{
            val arrayTitles: List<String> = it.characterTitle.split(",").map {it.trim()}
            val newOver = GOTResponse(id = "", name = it.characterName, image = "", house = it.characterHouse, titles = arrayTitles, father = it.characterFamily!!, mother = "")
            this.findNavController().navigate(FavoritesFragmentDirections.actionFavoritesFragmentToOverviewFragment(newOver))
        })

        return binding.root
    }
}
