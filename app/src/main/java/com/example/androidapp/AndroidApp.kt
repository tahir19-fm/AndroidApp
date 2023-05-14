package com.example.androidapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidApp:Application() {
    companion object {
        @JvmStatic
        var instance: AndroidApp? = null
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}