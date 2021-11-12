package com.bps.gotwinter2021.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.databinding.FragmentSearchResultsBinding
import com.bps.gotwinter2021.ui.search.adapter.CharacterAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {

    val viewModel: SearchResultsViewModel by viewModels()

    private lateinit var binding: FragmentSearchResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter = CharacterAdapter(CharacterAdapter.OnClickListener { character, identifier ->
            if (identifier == "navigate") {
                this.findNavController().navigate(
                    SearchResultsFragmentDirections.actionSearchResultsFragmentToOverviewFragment(
                        character
                    )
                )
            } else {
                viewModel.clickFavorite(character)
            }
        })
        var search =
            SearchResultsFragmentArgs.fromBundle(requireArguments()).searchString.toLowerCase().trim()
        search = viewModel.Capitalize(search)

        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_search_results, container, false
            )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.searchResultsRV.adapter = adapter
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.fetchCharactersByName(search)
        viewModel.searchString.observe(viewLifecycleOwner, {
            binding.searchText.text = it
        })
        viewModel.isFavorite.observe(viewLifecycleOwner, {
            adapter.setData(viewModel.characters.value,it)
        })
        viewModel.characters.observe(viewLifecycleOwner, {
            if (it != null) {
                viewModel.checkFavorite(it)
            }
        })
        viewModel.status.observe(viewLifecycleOwner, {
            when (it) {
                SearchResultsViewModel.GOTApiStatus.LOADING -> {
                    binding.searchResultsNotFoundTV.visibility = View.GONE
                    binding.searchResultsRV.visibility = View.GONE
                    binding.searchResultsLoadingIV.visibility = View.VISIBLE
                }
                SearchResultsViewModel.GOTApiStatus.DONE -> {
                    binding.searchResultsLoadingIV.visibility = View.GONE
                    binding.searchResultsNotFoundTV.visibility = View.GONE
                    binding.searchResultsRV.visibility = View.VISIBLE
                    binding.searchResultsSearchET.text = null
                }
                else -> {
                    binding.searchResultsLoadingIV.visibility = View.GONE
                    binding.searchResultsRV.visibility = View.GONE
                    binding.searchResultsNotFoundTV.visibility = View.VISIBLE
                }
            }
        })
        return binding.root
    }
}
