package com.bps.gotwinter2021.data.model

data class GOTResponse(
    val id: String,
    val name: String,
    val image: String,
    val house: String,
    val titles: List<title>,
    val father: String,
    val mother: String
)

data class title(
    val title: String
)