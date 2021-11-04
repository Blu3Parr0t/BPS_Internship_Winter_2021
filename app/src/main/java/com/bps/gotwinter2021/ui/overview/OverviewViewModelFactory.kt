package com.bps.gotwinter2021.ui.overview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao

class OverviewViewModelFactory(
    private val passedName: String,
    private val application: Application,
    private val database: FavoriteDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(passedName, application, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
