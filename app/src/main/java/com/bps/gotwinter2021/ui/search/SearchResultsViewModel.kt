package com.bps.gotwinter2021.ui.search

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchResultsViewModel(val repo: GOTRepo, val app: Application): ViewModel() {

    private val _searchText = MutableLiveData<String?>()
    val searchText: LiveData<String?> = _searchText

    private val _characters = MutableLiveData<GOTResponse?>()
    val characters: LiveData<GOTResponse?> = _characters

    private val dispatcher = Dispatchers.IO

    fun fetchCharactersByName(name: String){
        viewModelScope.launch(dispatcher) {
            when( val response = repo.fetchCharacterByName(dispatcher,name = name)){
                is ServiceResult.Succes -> {
                    _characters.postValue(response.data)
                    Log.d("Yoshi", "Succes: $response")
                }
                is ServiceResult.Error -> {
                    Timber.d("Error was found when calling GOT characters :: " + response.exception)

                    Log.d("Yoshi", "Error :${response}")
                }
                else -> {
                    Timber.d("Oh- oh... You've done fucked up...")
                    Log.d("Yoshi", "Fail: $response")
                }
            }
        }
    }

    fun updateSearch(text: CharSequence) {
        _searchText.value = text.toString()
    }

    fun searchButton() {
        val length = _searchText.value?.length ?: 0
        if (length > 0){
            fetchCharactersByName(_searchText.value.toString())
        }
        else{
            Toast.makeText(app?.applicationContext,"type what you want to search for", Toast.LENGTH_SHORT).show()
        }
    }
}
