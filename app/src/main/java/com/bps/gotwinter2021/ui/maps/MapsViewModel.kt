package com.bps.gotwinter2021.ui.maps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.common.secret.API.API_KEY
import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MapsViewModel(application: Application, val repo: GOTRepo)
    : AndroidViewModel(application){

    private val _theaterFeed = MutableLiveData<GOTTheatre?>()
    val theaterFeed: LiveData<GOTTheatre?>
        get() = _theaterFeed

    fun getStores(location: String, radius: Int, type: String,
                  key: String = API_KEY) {

        val dispatcher = Dispatchers.IO


        viewModelScope.launch(dispatcher) {
            when(val response = repo.fetchTheaters(dispatcher,
                location = location, radius = radius, type = type, key = key)) {
                is ServiceResult.Succes -> {
                    _theaterFeed.postValue(response.data)
                }

                is ServiceResult.Error -> {
                    Timber.d("Error was found when calling theaters :: " + response.exception)
                }

                else -> {
                    Timber.d("Uninstall life please")
                }
            }
        }
    }
}
