package com.bps.gotwinter2021.ui.house

import android.app.Application
import android.util.Log
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

class HouseViewModel(val application: Application, val repo: GOTRepo) : ViewModel() {

    private val _characterFromHouse = MutableLiveData<List<GOTResponse?>?>()
    val characterFromHouse: LiveData<List<GOTResponse?>?> = _characterFromHouse

    private val dispatcher = Dispatchers.IO

    fun fetchCharactersByHouse(house: String){
        viewModelScope.launch(dispatcher){
            when(val response = repo.fetchCharactersByHouse(dispatcher, house = house)){
                is ServiceResult.Succes -> {
                    _characterFromHouse.postValue(response.data)
                }
                is ServiceResult.Error -> {
                    Timber.d("Error retrieving: " + response.exception)
                }
                else -> {
                    Timber.d("ruh roh")
                }
            }
        }
    }
}
