package com.bps.gotwinter2021.di.main

import android.content.Context
import androidx.room.Room
import com.bps.gotwinter2021.favorites.database.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainComponent {

    @Singleton
    @Provides
    fun provideGOTDB(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        FavoriteDatabase::class.java,
        "favorite_characters_table"
    ).build()

    @Singleton
    @Provides
    fun provideGOTDBDao(db: FavoriteDatabase) = db.favoriteDatabaseDao
}
