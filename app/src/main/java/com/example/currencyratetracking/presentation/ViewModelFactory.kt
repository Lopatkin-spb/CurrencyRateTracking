package com.example.currencyratetracking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyratetracking.api_locale.api.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_remote.api.RatesApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.presentation.currencies.CurrenciesViewModel
import com.example.currencyratetracking.presentation.favorites.FavoritesViewModel


class ViewModelFactory(
    private val api: RatesApi,
    private val logger: BaseLogger,
    private val favoriteCurrencyPairApi: FavoriteCurrencyPairApi,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrenciesViewModel::class.java)) {
            return CurrenciesViewModel(
                api = api,
                logger = logger,
                favoriteCurrencyPairApi = favoriteCurrencyPairApi,
            ) as T
        } else if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(
                logger = logger,
                favoriteCurrencyPairApi = favoriteCurrencyPairApi,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}