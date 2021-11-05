package com.bps.gotwinter2021.data.network.endpoints

import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.model.GOTTheatre
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GOTApiEndPoint {
    @GET("api/show/characters/{name}")
    suspend fun fetchCharactersByName(
        @Path("name") name: String
    ):Response<GOTResponse?>

    @GET("/maps/api/place/nearbysearch/json")
    suspend fun getStores(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") key: String
    ): Response<GOTTheatre?>
}
