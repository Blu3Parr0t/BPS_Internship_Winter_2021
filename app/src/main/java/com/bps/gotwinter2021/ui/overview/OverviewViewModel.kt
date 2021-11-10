package com.bps.gotwinter2021.ui.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import kotlinx.coroutines.*

class OverviewViewModel(
    val application: Application, val dataSource: FavoriteDatabaseDao, val passedCharacterSA: GOTResponse
) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _passedCharacter = MutableLiveData<GOTResponse>()
    val passedCharacter: LiveData<GOTResponse>
        get() = _passedCharacter

    init{
        _passedCharacter.value = passedCharacterSA
        initializeFav()
    }

    private var fav = MutableLiveData<Favorite?>()

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
                newFav.characterTitle = oldResponse.titles[0]
                newFav.characterHouse = oldResponse.house
                newFav.characterFamily = oldResponse.father +", " + oldResponse.mother
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
}
