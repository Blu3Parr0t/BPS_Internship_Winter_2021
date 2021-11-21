package com.bps.gotwinter2021.ui.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val app: Application,
    private val GOTDBDao: FavoriteDatabaseDao,
    private val dispatcher: Dispatchers
    )
    : ViewModel() {

    private val _navYet = MutableLiveData<Boolean>()
    val navYet: LiveData<Boolean> = _navYet

    fun doneNav(){
        _navYet.value = false
    }
    fun justNav(){
        _navYet.value = true
    }

    private val _navigateOverview = MutableLiveData<Favorite>()
    val navigateOverview: LiveData<Favorite>
        get() = _navigateOverview

    fun navToOverview(character: Favorite){
        _navigateOverview.value = character
    }

    private val fav = MutableLiveData<Favorite?>()
    val favs = GOTDBDao.getAllCharacters()

    init{
        initializeFav()
    }

    private fun initializeFav() {
        viewModelScope.launch(dispatcher.IO) {
            fav.postValue(getFavFromDatabase())
        }
    }

    private suspend fun getFavFromDatabase(): Favorite? {
        return withContext(dispatcher.IO){
            var fav = GOTDBDao.getNewest()
            fav
        }
    }

    fun addClicked(oldResponse: Favorite){
        viewModelScope.launch(dispatcher.IO) {
            if(checkIfFavorited(oldResponse.characterName)){
                deleteOne(oldResponse.characterName)
            }else {
                val newFav = Favorite(
                    characterName = oldResponse.characterName,
                    characterTitle = oldResponse.characterTitle,
                    characterHouse = oldResponse.characterHouse,
                    characterFamily = oldResponse.characterFamily
                )
                insert(newFav)
            }
        }
    }

    private suspend fun deleteOne(name: String) {
        withContext(dispatcher.IO){
            GOTDBDao.clearOne(name)
        }
    }

    private suspend fun checkIfFavorited(name: String): Boolean {
        return withContext(dispatcher.IO){
            val check = GOTDBDao.findCharacter(name)
            check
        }
    }

    private suspend fun insert(newFav: Favorite) {
        withContext(dispatcher.IO){
            GOTDBDao.insert(newFav)
        }
    }
}
