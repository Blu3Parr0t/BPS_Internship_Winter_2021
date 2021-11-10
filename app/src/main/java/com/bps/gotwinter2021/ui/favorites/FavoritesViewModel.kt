package com.bps.gotwinter2021.ui.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import kotlinx.coroutines.*

class FavoritesViewModel(val application: Application, val dataSource: FavoriteDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateOverview = MutableLiveData<Favorite>()
    val navigateOverview: LiveData<Favorite>
        get() = _navigateOverview

    fun navToOverview(character: Favorite){
        _navigateOverview.value = character
    }

    private val fav = MutableLiveData<Favorite?>()
    val favs = dataSource.getAllCharacters()

    init{
        initializeFav()
    }

    private fun initializeFav() {
        coroutineScope.launch {
            fav.value = getFavFromDatabase()
        }
    }

    private suspend fun getFavFromDatabase(): Favorite? {
        return withContext(Dispatchers.IO){
            var fav = dataSource.getNewest()
            fav
        }
    }

    fun addClicked(oldResponse: Favorite){
        coroutineScope.launch {
            if(checkIfFavorited(oldResponse.characterName)){
                deleteOne(oldResponse.characterName)
            }else {
                val newFav = Favorite()
                newFav.characterName = oldResponse.characterName
                newFav.characterTitle = oldResponse.characterTitle
                newFav.characterHouse = oldResponse.characterName
                newFav.characterFamily = oldResponse.characterFamily
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
