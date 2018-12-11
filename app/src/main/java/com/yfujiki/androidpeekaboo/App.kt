package com.yfujiki.androidpeekaboo

import android.app.Application
import android.content.Context

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        gInstance = this
    }

    companion object {
        private var gInstance: App? = null

        fun getInstance() = gInstance!!

        fun getAppContext() = getInstance().applicationContext
    }
}