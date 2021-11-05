package com.bps.gotwinter2021.data.model

data class GOTTheatre (
    val results: List<Results>
)

data class Results (
    val place_id: String,
    val name: String,
    val geometry: Geometry,
    val price_level: Int,
)

data class Geometry (
    val location: Location
)

data class Location (
    val lat: Double,
    val lng: Double
)
