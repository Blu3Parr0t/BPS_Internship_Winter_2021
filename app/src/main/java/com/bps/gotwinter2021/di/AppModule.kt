package com.bps.gotwinter2021.di

import com.bps.gotwinter2021.data.network.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoroutines() = Dispatchers

    @Singleton
    @Provides
    fun provideIO() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideUI() = Dispatchers.Main

    //Glide

    //Gson

    //Retrofit
    @Singleton
    @Provides
    fun provideRetrofitFactory() = RetrofitFactory
}
