package com.bps.gotwinter2021.data.network.repo

import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repoimpl.GOTRepoImpl
import kotlinx.coroutines.CoroutineDispatcher

interface GOTRepo {
    suspend fun fetchCharacterByName(viewModelDispatcher: CoroutineDispatcher, name: String): ServiceResult<GOTResponse?>

    suspend fun fetchTheaters(viewModelDispatcher: CoroutineDispatcher, location: String,
                                       radius: Int, type: String, key: String)
            : ServiceResult<GOTTheatre?>

    companion object{
        fun provideGOTRepo(): GOTRepo{
            return GOTRepoImpl()
        }
    }
}
