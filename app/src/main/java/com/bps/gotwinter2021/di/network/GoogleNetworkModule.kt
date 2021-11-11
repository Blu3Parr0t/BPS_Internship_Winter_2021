package com.bps.gotwinter2021.di.network

import com.bps.gotwinter2021.data.network.endpoints.GoogleMapsApiEndPoint
import com.bps.gotwinter2021.data.network.repo.GoogleMapsRepo
import com.bps.gotwinter2021.data.network.repoimpl.GoogleMapsRepoImpl
import com.bps.gotwinter2021.data.network.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleNetworkModule {
    private const val GoogleMapsBaseUrl = "https://maps.googleapis.com/"

    @Singleton
    @Provides
    fun provideGoogleMapsRetrofit(): GoogleMapsApiEndPoint {
        return RetrofitFactory.retrofitProvider(
            GoogleMapsApiEndPoint::class.java,
            GoogleMapsBaseUrl
        )
    }

    @Singleton
    @Provides
    fun provideGoogleMapsRepo(dispatcher: Dispatchers, retroObject: GoogleMapsApiEndPoint): GoogleMapsRepo {
        return GoogleMapsRepoImpl(dispatcher, retroObject)
    }
}
