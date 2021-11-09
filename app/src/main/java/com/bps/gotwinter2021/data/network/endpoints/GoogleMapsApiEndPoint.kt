package com.bps.gotwinter2021.data.network.endpoints

import com.bps.gotwinter2021.data.model.GOTTheatre
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApiEndPoint {
    @GET("/maps/api/place/nearbysearch/json")
    suspend fun getStores(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") key: String
    ): Response<GOTTheatre?>
}
