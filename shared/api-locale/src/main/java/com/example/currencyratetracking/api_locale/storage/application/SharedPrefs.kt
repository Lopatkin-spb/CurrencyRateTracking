package com.example.currencyratetracking.api_locale.storage.application

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject


interface PrefsManager {

    fun get(): SharedPreferences

}

internal class AndroidPrefsManager @Inject constructor(private val context: Context) : PrefsManager {

    private val PREFERENCE_FILE_KEY = "com.example.currencyratetracking.shared.api_locale.PREFERENCE_FILE_KEY"

    override fun get(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
    }

}