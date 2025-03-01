package com.example.currencyratetracking.app

import android.app.Application
import com.example.currencyratetracking.di.app.AppComponent
import com.example.currencyratetracking.di.app.DaggerAppComponent


internal class CrtApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.factory().create(context = this)
    }

    fun getAppComponent(): AppComponent = appComponent

}