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
import kotlinx.coroutines.*
import timber.log.Timber

class HouseViewModel(
    val application: Application,
    val repo: GOTRepo,
    val dataSource: FavoriteDatabaseDao
) : ViewModel() {
    enum class GOTApiStatus { LOADING, ERROR, DONE }

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<HouseViewModel.GOTApiStatus>()
    val status: LiveData<HouseViewModel.GOTApiStatus> = _status

    private val _characterFromHouse = MutableLiveData<List<GOTResponse?>?>()
    val characterFromHouse: LiveData<List<GOTResponse?>?> = _characterFromHouse

    private val dispatcher = Dispatchers.IO

    private var fav = MutableLiveData<Favorite?>()

    private val _navigateOverview = MutableLiveData<GOTResponse>()
    val navigateOverview: LiveData<GOTResponse>
    get() = _navigateOverview

    fun navToOverview(character: GOTResponse){
        _navigateOverview.value = character
    }

    init{
        initializeFav()
    }

    private fun initializeFav() {
        coroutineScope.launch{
            fav.value = getFavFromDatabase()
        }
    }

    private suspend fun getFavFromDatabase(): Favorite? {
        return  withContext(Dispatchers.IO){
            var fav = dataSource.getNewest()
            fav
        }
    }

    fun addClicked(oldResponse: GOTResponse){
        coroutineScope.launch {
            if(checkIfFavorited(oldResponse.name)){
                deleteOne(oldResponse.name)
            }else {
                val newFav = Favorite()
                newFav.characterName = oldResponse.name
                if(oldResponse.titles.size > 0) {
                    newFav.characterTitle = oldResponse.titles[0]
                }
                newFav.characterImage = oldResponse.image
                newFav.characterHouse = oldResponse.house
                if(oldResponse.father.isNullOrEmpty() && oldResponse.mother.isNullOrEmpty()){
                    newFav.characterFamily = " "
                }
                else if(!oldResponse.father.isNullOrEmpty() && oldResponse.mother.isNullOrEmpty()){
                    newFav.characterFamily = oldResponse.father
                }
                else if(oldResponse.father.isNullOrEmpty() && !oldResponse.mother.isNullOrEmpty()){
                    newFav.characterFamily = oldResponse.mother
                }
                else{
                newFav.characterFamily = oldResponse.father +", " + oldResponse.mother}
                insert(newFav)
            }
        }
    }

    private suspend fun deleteOne(name: String) {
        withContext(Dispatchers.IO){
            dataSource.clearOne(name)
        }
    }

    private suspend fun checkIfFavorited(name: String): Boolean {
        return withContext(Dispatchers.IO){
            val check = dataSource.findCharacter(name)
            check
        }
    }

    private suspend fun insert(newFav: Favorite) {
        withContext(Dispatchers.IO){
            dataSource.insert(newFav)
        }
    }

    fun fetchCharactersByHouse(house: String){
        viewModelScope.launch(dispatcher){
            when(val response = repo.fetchCharactersByHouse(dispatcher, house = house)){
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
