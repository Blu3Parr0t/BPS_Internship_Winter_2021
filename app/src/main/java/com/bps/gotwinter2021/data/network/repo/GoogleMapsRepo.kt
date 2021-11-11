package com.bps.gotwinter2021.data.network.repo

import com.bps.gotwinter2021.data.model.GOTTheatre
import com.bps.gotwinter2021.data.network.endpoints.GoogleMapsApiEndPoint
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repoimpl.GoogleMapsRepoImpl
import kotlinx.coroutines.Dispatchers

interface GoogleMapsRepo {
    suspend fun fetchTheaters(
        location: String,
        radius: Int, type: String, key: String
    )
            : ServiceResult<GOTTheatre?>

    companion object {
        fun provideGoogleMapsRepo(
            dispatcher: Dispatchers,
            retroObject: GoogleMapsApiEndPoint
        ): GoogleMapsRepo {
            return GoogleMapsRepoImpl(dispatcher, retroObject)
        }
    }
}
