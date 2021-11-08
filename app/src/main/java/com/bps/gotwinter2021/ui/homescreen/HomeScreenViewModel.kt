package com.bps.gotwinter2021.ui.homescreen

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val app: Application,
    private val dispatchers: Dispatchers,
    private val GOTRepo: GOTRepo
) : ViewModel() {

    private val _searchText = MutableLiveData<String?>()
    val searchText: LiveData<String?> = _searchText

    private val _navigateToSearchResults = MutableLiveData<Boolean>()
    val navigateToSearchResults: LiveData<Boolean> = _navigateToSearchResults

    fun diTestFunction() {
        viewModelScope.launch(dispatchers.IO) {
            GOTRepo.fetchCharactersByHouse("House Stark")
        }
    }

    fun updateSearch(text: CharSequence) {
        _searchText.value = text.toString()
    }

    fun searchButton() {
        val length = _searchText.value?.length ?: 0
        if (length > 0) {
            _navigateToSearchResults.value = true
        } else {
            Toast.makeText(
                app.applicationContext,
                "type what you want to search for",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun doneNavigation() {
        _navigateToSearchResults.value = false
    }
}
