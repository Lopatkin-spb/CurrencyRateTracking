package com.example.currencyratetracking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyratetracking.api_remote.api.RatesApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.presentation.currencies.CurrenciesViewModel
import com.example.currencyratetracking.presentation.favorites.FavoritesViewModel


class ViewModelFactory(
    private val api: RatesApi,
    private val logger: BaseLogger,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrenciesViewModel::class.java)) {
            return CurrenciesViewModel(
                api = api,
                logger = logger,
            ) as T
        } else if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(
                logger = logger,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}