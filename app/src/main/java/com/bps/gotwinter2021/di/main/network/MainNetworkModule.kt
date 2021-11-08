package com.bps.gotwinter2021.di.main.network

import com.bps.gotwinter2021.data.network.endpoints.GOTApiEndPoint
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.bps.gotwinter2021.data.network.repoimpl.GOTRepoImpl
import com.bps.gotwinter2021.data.network.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainNetworkModule {
    private const val GOTBaseUrl = "https://api.got.show/"

    @Singleton
    @Provides
    fun provideGOTRetrofit(): GOTApiEndPoint {
        return RetrofitFactory.retrofitProvider(
            GOTApiEndPoint::class.java,
            GOTBaseUrl
        )
    }

    @Singleton
    @Provides
    fun provideGOTRepo(dispatcher: Dispatchers, retroObject: GOTApiEndPoint): GOTRepo {
        return GOTRepoImpl(dispatcher, retroObject)
    }
}
