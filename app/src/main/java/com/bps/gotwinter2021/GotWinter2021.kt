package com.bps.gotwinter2021

import android.app.Application
import timber.log.Timber

class GotWinter2021: Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
