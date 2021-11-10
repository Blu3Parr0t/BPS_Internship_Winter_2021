package com.bps.gotwinter2021.favorites.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_characters_table")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var characterId: Long = 0L,

    @ColumnInfo(name = "character_name")
    var characterName: String = "",

    @ColumnInfo(name = "character_image")
    var characterImage: String = "",

    @ColumnInfo(name = "character_title")
    var characterTitle: String = "",

    @ColumnInfo(name = "family_members")
    var characterFamily: String = "",

    @ColumnInfo(name = "character_house")
    var characterHouse: String = ""
)
