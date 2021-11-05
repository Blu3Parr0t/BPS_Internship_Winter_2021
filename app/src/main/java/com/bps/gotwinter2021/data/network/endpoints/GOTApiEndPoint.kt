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

    @GET("/api/show/characters/byHouse/{house}")
    suspend fun fetchCharactersByHouse(
        @Path("house") house: String
    ) : Response<List<GOTResponse?>?>
}
