package com.bps.gotwinter2021.data.network.repoimpl

import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.endpoints.GOTApiEndPoint
import com.bps.gotwinter2021.data.network.endpoints.GoogleMapsApiEndPoint
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GoogleMapsRepo
import com.bps.gotwinter2021.data.network.retrofit.RetrofitFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GoogleMapsRepoImpl(): GoogleMapsRepo {

    val retroTheatreObject = RetrofitFactory.retrofitProvider(
        GoogleMapsApiEndPoint::class.java,
        "https://maps.googleapis.com/"
    )

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
