package com.bps.gotwinter2021.data.network.repo

import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repoimpl.GOTRepoImpl
import kotlinx.coroutines.CoroutineDispatcher

interface GOTRepo {
    suspend fun fetchCharacterByName(viewModelDispatcher: CoroutineDispatcher, name: String): ServiceResult<GOTResponse?>

    suspend fun fetchCharactersByHouse(viewModelDispatcher: CoroutineDispatcher, house: String): ServiceResult<List<GOTResponse?>?>

    companion object{
        fun provideGOTRepo(): GOTRepo{
            return GOTRepoImpl()
        }
    }
}
