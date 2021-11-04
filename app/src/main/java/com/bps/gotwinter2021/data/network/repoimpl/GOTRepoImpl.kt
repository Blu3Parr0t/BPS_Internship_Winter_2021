package com.bps.gotwinter2021.data.network.repoimpl

import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.network.endpoints.GOTApiEndPoint
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.data.network.retrofit.RetrofitFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GOTRepoImpl(): GOTRepo {
    val retroObject = RetrofitFactory.retrofitProvider(
        GOTApiEndPoint::class.java,
        "https://api.got.show/"
    )

    override suspend fun fetchCharacterByName(
        viewmodelDispatcher: CoroutineDispatcher,
        name: String
    ): ServiceResult<List<GOTResponse>?> {
        return  withContext(viewmodelDispatcher){
            val dataResponse = retroObject.getCharacterByName(name = name)
            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}