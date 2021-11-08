package com.bps.gotwinter2021.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.data.network.endpoints.GOTApiEndPoint
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.databinding.FragmentSearchResultsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {
    @Inject lateinit var retroObject: GOTApiEndPoint

    val viewModel: SearchResultsViewModel by lazy {
        createViewModel {
            SearchResultsViewModel(
                GOTRepo.provideGOTRepo(this.viewModel.dispatcher, retroObject),
                requireActivity().application
            )
        }
    }

    private lateinit var binding: FragmentSearchResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_results, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val search = SearchResultsFragmentArgs.fromBundle(requireArguments()).searchString
        viewModel.fetchCharactersByName(search)
        return binding.root
    }
}
