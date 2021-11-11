package com.bps.gotwinter2021.ui.search

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.favorites.database.Favorite
import com.bps.gotwinter2021.favorites.database.FavoriteDatabase
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val app: Application,
    private val GOTRepo: GOTRepo,
    private val GOTDBDao: FavoriteDatabaseDao
) : ViewModel() {

    enum class GOTApiStatus { LOADING, ERROR, DONE }

    private val _searchText = MutableLiveData<String?>()
    val searchText: LiveData<String?> = _searchText

    private val _characters = MutableLiveData<GOTResponse?>()
    val characters: LiveData<GOTResponse?> = _characters

    private val _status = MutableLiveData<GOTApiStatus>()
    val status: LiveData<GOTApiStatus> = _status

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String> = _searchString

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite


    private val dispatcher = Dispatchers.IO

    fun fetchCharactersByName(name: String) {
        viewModelScope.launch(dispatcher) {
            when (val response = GOTRepo.fetchCharacterByName(name = name)) {
                is ServiceResult.Succes -> {
                    _characters.postValue(response.data)
                    _status.postValue(GOTApiStatus.DONE)
                }
                is ServiceResult.Error -> {
                    Timber.d(
                        "Error was found when calling GOT characters :: "
                                + response.exception
                    )
                    _status.postValue(GOTApiStatus.ERROR)
                }
                else -> {
                    Timber.d("Oh- oh... Something is wrong...")
                    _status.postValue(GOTApiStatus.ERROR)
                }
            }
            _searchString.postValue(
                app.applicationContext?.getString(
                    R.string.search_screen_subtitle,
                    name
                )
            )
        }
    }

    fun updateSearch(text: CharSequence) {
        _searchText.value = text.toString()
    }

    fun searchButton() {
        _status.value = GOTApiStatus.LOADING
        val length = _searchText.value?.length ?: 0
        if (length > 0) {
            fetchCharactersByName(
                Capitalize(_searchText.value.toString())
            )
        } else {
            Toast.makeText(
                app.applicationContext,
                "type what you want to search for",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun Capitalize(str: String): String {

        val words = str.split(" ").toMutableList()

        var output = ""

        for (word in words) {
            output += word.capitalize() + " "
        }

        output = output.trim()

        return output
    }

    fun clickFavorite(character: GOTResponse) {
        viewModelScope.launch {
            checkFavorite(character)
            if (_isFavorite.value == true) {
                deleteFavorite(character)
            } else {
                addFavorite(character)
            }
        }
    }

    fun checkFavorite(character: GOTResponse) {
        viewModelScope.launch {
            _isFavorite.value = isFavorite(character)
        }
    }

    private suspend fun addFavorite(character: GOTResponse) {
        withContext(Dispatchers.IO) {
            val addCharacter = Favorite()
            addCharacter.characterName = character.name
            addCharacter.characterHouse = character.house
            addCharacter.characterTitle = character.titles[0]
            addCharacter.characterFamily = character.father + character.mother
            addCharacter.characterImage = character.image
            GOTDBDao.insert(addCharacter)
        }
    }

    private suspend fun deleteFavorite(character: GOTResponse) {
        withContext(Dispatchers.IO) {
            GOTDBDao.clearOne(character.name)
        }
    }

    private suspend fun isFavorite(character: GOTResponse): Boolean {
        return withContext(Dispatchers.IO) {
            GOTDBDao.findCharacter(character.name)
        }
    }
}

