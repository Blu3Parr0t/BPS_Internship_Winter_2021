package com.bps.gotwinter2021.data.network.repoimpl

import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.endpoints.GOTApiEndPoint
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.data.network.retrofit.RetrofitFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class GOTRepoImpl @Inject constructor(
    private val dispatcher: Dispatchers,
    private val retroObject: GOTApiEndPoint
) : GOTRepo {

    override suspend fun fetchCharactersByHouse(
        house: String
    ): ServiceResult<List<GOTResponse?>?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.fetchCharactersByHouse(house = house)
            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun fetchCharacterByName(
        name: String
    ): ServiceResult<GOTResponse?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.fetchCharactersByName(name = name)
            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}
