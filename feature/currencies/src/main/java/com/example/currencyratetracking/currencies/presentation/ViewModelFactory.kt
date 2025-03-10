package com.example.currencyratetracking.currencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyratetracking.api_locale.storage.application.DatabaseApiManager
import com.example.currencyratetracking.api_remote.api.ApiRemoteManager
import com.example.currencyratetracking.common_android.BaseLogger


class ViewModelFactory(
    private val apiRemoteManager: ApiRemoteManager,
    private val databaseApiManager: DatabaseApiManager,
    private val logger: BaseLogger,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrenciesViewModel::class.java)) {
            return CurrenciesViewModel(
                api = apiRemoteManager.getRatesApi(),
                logger = logger,
                favoriteCurrencyPairApi = databaseApiManager.getFavoriteCurrencyPairApi(),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}