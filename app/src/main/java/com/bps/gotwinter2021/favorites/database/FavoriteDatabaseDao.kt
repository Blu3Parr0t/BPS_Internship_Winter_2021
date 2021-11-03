package com.bps.gotwinter2021.favorites.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDatabaseDao {
    @Insert
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    //find a specific character based on its name
    @Query("SELECT * from favorite_characters_table WHERE character_name = :key")
    fun findCharacter(key: String) : Favorite

    //delete all from table
    @Query("DELETE FROM favorite_characters_table")
    fun clearAll()

    //delete one character found by its name
    @Query("DELETE FROM favorite_characters_table WHERE character_name = :characterName")
    fun clearOne(characterName: String)

    //get a list of every character in the favorites database
    @Query("SELECT * FROM favorite_characters_table ORDER BY characterId DESC")
    fun getAllCharacters() : LiveData<List<Favorite>>

    //get last character added to database
    @Query("SELECT * FROM favorite_characters_table ORDER BY characterId DESC LIMIT 1")
    fun getNewest(): Favorite?
}
