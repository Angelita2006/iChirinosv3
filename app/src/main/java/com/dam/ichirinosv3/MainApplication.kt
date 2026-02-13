package com.dam.ichirinosv3

import android.app.Application
import com.dam.ichirinosv3.data.AppContainer
import com.dam.ichirinosv3.data.DefaultAppContainer

class MainApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}
