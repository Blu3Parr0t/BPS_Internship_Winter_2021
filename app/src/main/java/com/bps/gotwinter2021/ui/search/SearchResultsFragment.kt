package com.bps.gotwinter2021.ui.search

import android.os.Bundle
import android.text.TextUtils.split
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.databinding.FragmentSearchResultsBinding
import com.bps.gotwinter2021.ui.search.adapter.CharacterAdapter

class SearchResultsFragment : Fragment() {

    val viewModel: SearchResultsViewModel by lazy {
        createViewModel {
            SearchResultsViewModel(
                GOTRepo.provideGOTRepo(),
                requireActivity().application
            )
        }
    }

    private lateinit var binding: FragmentSearchResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter = CharacterAdapter()
        var search = SearchResultsFragmentArgs.fromBundle(requireArguments()).searchString.toLowerCase()
        search = viewModel.Capitalize(search)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_results, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.searchResultsRV.adapter = adapter

        viewModel.fetchCharactersByName(search)
        viewModel.characters.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
        viewModel.searchString.observe(viewLifecycleOwner, {
            binding.searchText.text = it
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
