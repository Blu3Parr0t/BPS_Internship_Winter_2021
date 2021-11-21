package com.bps.gotwinter2021.ui.house

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.databinding.HouseFragmentBinding
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HouseViewModel @Inject constructor(
    private val app: Application,
    private val GOTRepo: GOTRepo,
    private val GOTDBDao: FavoriteDatabaseDao,
    private val dispatcher: Dispatchers
) : ViewModel() {
    enum class GOTApiStatus { LOADING, ERROR, DONE }

    private val _navYet = MutableLiveData<Boolean>()
    val navYet: LiveData<Boolean> = _navYet

    private val _status = MutableLiveData<GOTApiStatus>()
    val status: LiveData<GOTApiStatus> = _status

    private val _characterFromHouse = MutableLiveData<List<GOTResponse?>?>()
    val characterFromHouse: LiveData<List<GOTResponse?>?> = _characterFromHouse


    private var fav = MutableLiveData<Favorite?>()

    private val _navigateOverview = MutableLiveData<GOTResponse>()
    val navigateOverview: LiveData<GOTResponse>
        get() = _navigateOverview

    fun navToOverview(character: GOTResponse) {
        _navigateOverview.value = character
    }

    fun doneNav(){
        _navYet.value = false
    }
    fun justNav(){
        _navYet.value = true
    }

    init {
        initializeFav()
    }

    private fun initializeFav() {
        viewModelScope.launch(dispatcher.IO) {
            fav.postValue(getFavFromDatabase())
        }
    }

    private suspend fun getFavFromDatabase(): Favorite? {
        return withContext(dispatcher.IO) {
            var fav = GOTDBDao.getNewest()
            fav
        }
    }

    fun addClicked(oldResponse: GOTResponse) {
        viewModelScope.launch(dispatcher.IO) {
            if (checkIfFavorited(oldResponse.name)) {
                deleteOne(oldResponse.name)
            } else {
                val newFav = Favorite(
                    characterName = oldResponse.name,
                    characterHouse = oldResponse.house,
                    characterImage = oldResponse.image
                )
                if (oldResponse.titles.size > 0) {
                    newFav.characterTitle = oldResponse.titles[0]
                } else {
                    newFav.characterTitle = " "
                }

                //logic to avoid passing empty value
                if (oldResponse.father.isNullOrEmpty() && oldResponse.mother.isNullOrEmpty()) {
                    newFav.characterFamily = " "
                } else if (!oldResponse.father.isNullOrEmpty() && oldResponse.mother.isNullOrEmpty()) {
                    newFav.characterFamily = oldResponse.father
                } else if (oldResponse.father.isNullOrEmpty() && !oldResponse.mother.isNullOrEmpty()) {
                    newFav.characterFamily = oldResponse.mother
                } else {
                    newFav.characterFamily = oldResponse.father + ", " + oldResponse.mother
                }
                insert(newFav)
            }
        }
    }

    private suspend fun deleteOne(name: String) {
        withContext(dispatcher.IO) {
            GOTDBDao.clearOne(name)
        }
    }

    private suspend fun checkIfFavorited(name: String): Boolean {
        return withContext(dispatcher.IO) {
            val check = GOTDBDao.findCharacter(name)
            check
        }
    }

    private suspend fun insert(newFav: Favorite) {
        withContext(dispatcher.IO) {
            GOTDBDao.insert(newFav)
        }
    }

    fun fetchCharactersByHouse(house: String) {
        viewModelScope.launch(dispatcher.IO) {
            when (val response = GOTRepo.fetchCharactersByHouse(house = house)) {
                is ServiceResult.Succes -> {
                    _characterFromHouse.postValue(response.data)
                    _status.postValue(GOTApiStatus.DONE)
                }
                is ServiceResult.Error -> {
                    Timber.d("Error retrieving: " + response.exception)
                    _status.postValue(GOTApiStatus.ERROR)
                }
                else -> {
                    Timber.d("ruh roh")
                    _status.postValue(GOTApiStatus.ERROR)
                }
            }
        }
    }

    fun setBackground(houseSelected: String): Int {
        when(houseSelected){
            "Lannister"-> return R.drawable.background_house_lannister_blur
            "Targaryen"-> return (R.drawable.background_targaryen_blur)
            "Baratheon"-> return (R.drawable.background_baratheon_blur)
            else -> return (R.drawable.blurred_fav_bg)
        }
    }
}
