package com.bps.gotwinter2021.ui.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val app: Application,
    private val GOTDBDao: FavoriteDatabaseDao
) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _passedCharacter = MutableLiveData<GOTResponse>()
    val passedCharacter: LiveData<GOTResponse>
        get() = _passedCharacter

    fun getPassedArg(passedCharacter: GOTResponse){
        _passedCharacter.value = passedCharacter
    }
    init{
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
            var fav = GOTDBDao.getNewest()
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
            GOTDBDao.clearOne(name)
        }
    }

    private suspend fun checkIfFavorited(name: String): Boolean {
        return withContext(Dispatchers.IO){
            val check = GOTDBDao.findCharacter(name)
            check
        }
    }

    private suspend fun insert(newFav: Favorite) {
        withContext(Dispatchers.IO){
            GOTDBDao.insert(newFav)
        }
    }
}
