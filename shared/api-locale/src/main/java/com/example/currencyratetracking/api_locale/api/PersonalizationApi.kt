package com.example.currencyratetracking.api_locale.api

import com.example.currencyratetracking.api_locale.storage.application.PrefsManager
import javax.inject.Inject


interface PersonalizationApi {

    fun getUserSelectedBaseCurrency(): String

    fun setUserSelectedBaseCurrency(currency: String): Boolean

}


internal class PreferencesPersonalizationApi @Inject constructor(private val prefs: PrefsManager) : PersonalizationApi {

    companion object {
        private const val USER_SELECTED_BASE_CURRENCY_KEY =
            "com.example.currencyratetracking.USER_SELECTED_BASE_CURRENCY_KEY"
        private const val DEFAULT_EMPTY_STRING = ""
    }

    override fun getUserSelectedBaseCurrency(): String {
        val text = prefs.get().getString(USER_SELECTED_BASE_CURRENCY_KEY, DEFAULT_EMPTY_STRING) ?: DEFAULT_EMPTY_STRING
        return text
    }

    override fun setUserSelectedBaseCurrency(currency: String): Boolean {
        val isSuccess = prefs.get()
            .edit()
            .putString(USER_SELECTED_BASE_CURRENCY_KEY, currency)
            .commit()
//        if (isSuccess) {
//            return isSuccess
//        } else {
//            throw Exception("Value not insert (currency = $currency)")
//        }
        return isSuccess
    }

}