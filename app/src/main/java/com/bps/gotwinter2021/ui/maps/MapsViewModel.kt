package com.bps.gotwinter2021.ui.maps

import androidx.lifecycle.*
import com.bps.gotwinter2021.common.secret.API.API_KEY
import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GoogleMapsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val GoogleMapsRepo: GoogleMapsRepo,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private val _theaterFeed = MutableLiveData<GOTTheatre?>()
    val theaterFeed: LiveData<GOTTheatre?>
        get() = _theaterFeed

    fun getStores(
        location: String, radius: Int, type: String,
        key: String = API_KEY
    ) {
        viewModelScope.launch(dispatchers.IO) {
            when (val response = GoogleMapsRepo.fetchTheaters(
                location = location, radius = radius, type = type, key = key
            )) {
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
