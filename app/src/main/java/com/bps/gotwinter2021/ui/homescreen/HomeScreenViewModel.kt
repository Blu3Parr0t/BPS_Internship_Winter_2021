package com.bps.gotwinter2021.ui.homescreen

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeScreenViewModel(val app: Application): ViewModel() {

    private val _searchText = MutableLiveData<String?>()
    val searchText: LiveData<String?> = _searchText

    fun updateSearch(text: CharSequence) {
        _searchText.value = text.toString()
    }

    fun searchButton() {
        val length = _searchText.value?.length ?: 0
        if (length > 0){
            Toast.makeText(app?.applicationContext,"search for ${searchText.value}", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(app?.applicationContext,"type what you want to search for", Toast.LENGTH_SHORT).show()
        }
    }
}