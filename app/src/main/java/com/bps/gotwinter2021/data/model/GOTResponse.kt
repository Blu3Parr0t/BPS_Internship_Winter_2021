package com.bps.gotwinter2021.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GOTResponse(
    val id: String,
    val name: String,
    val image: String,
    val house: String,
    val titles: List<String>,
    val father: String,
    val mother: String
) : Parcelable
