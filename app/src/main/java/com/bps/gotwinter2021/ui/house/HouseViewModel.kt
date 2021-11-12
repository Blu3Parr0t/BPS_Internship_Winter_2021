package com.bps.gotwinter2021.ui.house

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GOTRepo
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
    private val GOTDBDao: FavoriteDatabaseDao
) : ViewModel() {
    enum class GOTApiStatus { LOADING, ERROR, DONE }

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navYet = MutableLiveData<Boolean>()
    val navYet: LiveData<Boolean> = _navYet

    private val _status = MutableLiveData<GOTApiStatus>()
    val status: LiveData<GOTApiStatus> = _status

    private val _characterFromHouse = MutableLiveData<List<GOTResponse?>?>()
    val characterFromHouse: LiveData<List<GOTResponse?>?> = _characterFromHouse

    private val dispatcher = Dispatchers.IO

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
        coroutineScope.launch {
            fav.value = getFavFromDatabase()
        }
    }

    private suspend fun getFavFromDatabase(): Favorite? {
        return withContext(Dispatchers.IO) {
            var fav = GOTDBDao.getNewest()
            fav
        }
    }

    fun addClicked(oldResponse: GOTResponse) {
        coroutineScope.launch {
            if (checkIfFavorited(oldResponse.name)) {
                deleteOne(oldResponse.name)
            } else {
                val newFav = Favorite()
                newFav.characterName = oldResponse.name

                if (oldResponse.titles.size > 0) {
                    newFav.characterTitle = oldResponse.titles[0]
                } else {
                    newFav.characterTitle = " "
                }

                newFav.characterImage = oldResponse.image
                newFav.characterHouse = oldResponse.house

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
        withContext(Dispatchers.IO) {
            GOTDBDao.clearOne(name)
        }
    }

    private suspend fun checkIfFavorited(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            val check = GOTDBDao.findCharacter(name)
            check
        }
    }

    private suspend fun insert(newFav: Favorite) {
        withContext(Dispatchers.IO) {
            GOTDBDao.insert(newFav)
        }
    }

    fun fetchCharactersByHouse(house: String) {
        viewModelScope.launch(dispatcher) {
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
}
