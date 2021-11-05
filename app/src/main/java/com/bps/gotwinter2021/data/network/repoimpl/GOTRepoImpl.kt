package com.bps.gotwinter2021.data.network.repoimpl

import android.util.Log
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.model.GOTTheatre
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

    val retroTheatreObject = RetrofitFactory.retrofitProvider(
        GOTApiEndPoint::class.java,
        "https://maps.googleapis.com/"
    )

    override suspend fun fetchCharacterByName(
        viewmodelDispatcher: CoroutineDispatcher,
        name: String
    ): ServiceResult<GOTResponse?> {
        return  withContext(viewmodelDispatcher){
            val dataResponse = retroObject.fetchCharactersByName(name = name)
            Log.d("Yoshi", "$dataResponse")
            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun fetchTheaters(
        viewModelDispatcher: CoroutineDispatcher,
        location: String,
        radius: Int,
        type: String,
        key: String
    ): ServiceResult<GOTTheatre?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroTheatreObject.getStores(location = location, radius = radius, type = type, key = key)

            if(dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}
