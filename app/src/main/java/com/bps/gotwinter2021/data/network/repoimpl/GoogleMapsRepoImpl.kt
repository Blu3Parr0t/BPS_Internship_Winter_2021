package com.bps.gotwinter2021.data.network.repoimpl

import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.endpoints.GoogleMapsApiEndPoint
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repo.GoogleMapsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleMapsRepoImpl @Inject constructor(
    private val dispatcher: Dispatchers,
    private val retroObject: GoogleMapsApiEndPoint
) : GoogleMapsRepo {

    override suspend fun fetchTheaters(
        location: String,
        radius: Int,
        type: String,
        key: String
    ): ServiceResult<GOTTheatre?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.getStores(
                location = location,
                radius = radius,
                type = type,
                key = key
            )

            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}
