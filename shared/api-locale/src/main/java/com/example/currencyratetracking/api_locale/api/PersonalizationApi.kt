package com.example.currencyratetracking.api_locale.api

import com.example.currencyratetracking.api_locale.storage.application.PrefsManager
import javax.inject.Inject


interface PersonalizationApi {

    fun getBaseSelectedCurrency(): String

    fun insertBaseSelectedCurrency(place: String): Boolean

}


internal class PreferencesPersonalizationApi @Inject constructor(private val prefs: PrefsManager) : PersonalizationApi {

    companion object {
        private const val BASE_SELECTED_CURRENCY_KEY = "com.example.currencyratetracking.BASE_SELECTED_CURRENCY_KEY"
        private const val DEFAULT_EMPTY_STRING = ""
    }

    override fun getBaseSelectedCurrency(): String {
        val text = prefs.get().getString(BASE_SELECTED_CURRENCY_KEY, DEFAULT_EMPTY_STRING) ?: DEFAULT_EMPTY_STRING
        return text
    }

    override fun insertBaseSelectedCurrency(data: String): Boolean {
        val isSuccess = prefs.get()
            .edit()
            .putString(BASE_SELECTED_CURRENCY_KEY, data)
            .commit()
        if (isSuccess) {
            return isSuccess
        } else {
            throw Exception("Value not insert (value = $data)")
        }
    }

}