package com.bps.gotwinter2021.ui.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao

class OverviewViewModel(
    val application: Application, val database: FavoriteDatabaseDao, val passedCharacterSA: GOTResponse
) : ViewModel() {
    private val _passedCharacter = MutableLiveData<GOTResponse>()
    val passedCharacter: LiveData<GOTResponse>
        get() = _passedCharacter
    init{
        _passedCharacter.value = passedCharacterSA
    }
}
