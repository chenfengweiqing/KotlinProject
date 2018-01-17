package com.example.simplecalender

import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}
