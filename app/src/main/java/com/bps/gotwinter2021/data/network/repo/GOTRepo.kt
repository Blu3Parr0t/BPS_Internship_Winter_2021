package com.bps.gotwinter2021.data.network.repo

import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.endpoints.GOTApiEndPoint
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repoimpl.GOTRepoImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface GOTRepo {
    suspend fun fetchCharacterByName(
        viewModelDispatcher: CoroutineDispatcher,
        name: String
    ): ServiceResult<GOTResponse?>

    suspend fun fetchCharactersByHouse(
        house: String
    ): ServiceResult<List<GOTResponse?>?>

    companion object {
        fun provideGOTRepo(dispatcher: Dispatchers, retroObject: GOTApiEndPoint): GOTRepo {
            return GOTRepoImpl(dispatcher, retroObject)
        }
    }
}
