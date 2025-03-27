package com.example.currencyratetracking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_remote.api.RatesApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.presentation.favorites.FavoritesViewModel


class ViewModelFactory(
    private val api: RatesApi,
    private val logger: BaseLogger,
    private val favoriteCurrencyPairApi: FavoriteCurrencyPairApi,
    private val dispatcher: BaseCoroutineDispatcher,
    private val clearUserSessionByLiveCycleUseCase: ClearUserSessionByLiveCycleUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(
                logger = logger,
                favoriteCurrencyPairApi = favoriteCurrencyPairApi,
            ) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                logger = logger,
                dispatcher = dispatcher,
                clearUserSessionByLiveCycleUseCase = clearUserSessionByLiveCycleUseCase,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}