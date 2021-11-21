package com.bps.gotwinter2021.ui.overview

import android.app.Application
import android.text.Html
import android.text.Spanned
import androidx.core.text.toSpanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.databinding.OverviewFragmentBinding
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val app: Application,
    private val GOTDBDao: FavoriteDatabaseDao,
    private val dispatcher: Dispatchers
) : ViewModel() {

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
        viewModelScope.launch(dispatcher.IO) {
            fav?.postValue(getFavFromDatabase())
        }
    }

    private suspend fun getFavFromDatabase(): Favorite? {
        return  withContext(dispatcher.IO){
            var fav = GOTDBDao.getNewest()
            fav
        }
    }

    fun determineFamily(passedCharacter: GOTResponse):Spanned {
        if(passedCharacter.father.isNullOrEmpty() && passedCharacter.mother.isNullOrEmpty()) {
            return " ".toSpanned()
        }
        else if(passedCharacter.father.isNullOrEmpty() && !passedCharacter.mother.isNullOrEmpty() && passedCharacter.mother != " "){
            val justM: String = "<b> Family: </b>" + passedCharacter.mother
            return Html.fromHtml(justM)
        }
        else if(!passedCharacter.father.isNullOrEmpty() && passedCharacter.mother.isNullOrEmpty() && passedCharacter.father != " "){
            val justF: String = "<b> Family: </b>" + passedCharacter.father
            return Html.fromHtml(justF)
        }
        else if(passedCharacter.father == " " || passedCharacter.mother == " "){
            return " ".toSpanned()
        }
        else{
            val family: String = "<b> Family: </b>" + passedCharacter.father +", " + passedCharacter.mother
            return Html.fromHtml(family)
        }
    }

    fun setTitle(passedCharacter: GOTResponse): Spanned {
        if(passedCharacter.titles.size > 0) {
            if(passedCharacter.titles[0] == " "){
                return " ".toSpanned()
            }else {
                val title: String = "<b> Title: </b>" + passedCharacter.titles[0]
                return Html.fromHtml(title)
            }
        }
        return " ".toSpanned()
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
