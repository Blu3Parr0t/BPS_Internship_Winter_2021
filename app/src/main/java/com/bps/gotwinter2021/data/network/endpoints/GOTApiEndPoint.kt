package com.bps.gotwinter2021.data.network.endpoints

import com.bps.gotwinter2021.data.model.GOTResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GOTApiEndPoint {
    @GET("api/show/characters/{name}")
    suspend fun fetchCharactersByName(
        @Path("name") name: String
    ):Response<GOTResponse?>
}
