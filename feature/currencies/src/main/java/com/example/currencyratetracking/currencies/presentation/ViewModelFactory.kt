package com.example.currencyratetracking.currencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.currencies.domain.*


class ViewModelFactory(
    private val logger: BaseLogger,
    private val getListBaseCurrenciesUseCase: GetListBaseCurrenciesUseCase,
    private val setPairCurrenciesToFavoriteUseCase: SetPairCurrenciesToFavoriteUseCase,
    private val deletePairCurrenciesFromFavoriteByCharCodesUseCase: DeletePairCurrenciesFromFavoriteByCharCodesUseCase,
    private val getListActualCurrencyRatesByBaseCharCodeUseCase: GetListActualCurrencyRatesByBaseCharCodeUseCase,
    private val dispatcher: BaseCoroutineDispatcher,
    private val getUserSelectedBaseCurrencyUseCase: GetUserSelectedBaseCurrencyUseCase,
    private val setUserSelectedBaseCurrencyUseCase: SetUserSelectedBaseCurrencyUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrenciesViewModel::class.java)) {
            return CurrenciesViewModel(
                logger = logger,
                getListBaseCurrenciesUseCase = getListBaseCurrenciesUseCase,
                dispatcher = dispatcher,
                setPairCurrenciesToFavoriteUseCase = setPairCurrenciesToFavoriteUseCase,
                deletePairCurrenciesFromFavoriteByCharCodesUseCase = deletePairCurrenciesFromFavoriteByCharCodesUseCase,
                getListActualCurrencyRatesByBaseCharCodeUseCase = getListActualCurrencyRatesByBaseCharCodeUseCase,
                getUserSelectedBaseCurrencyUseCase = getUserSelectedBaseCurrencyUseCase,
                setUserSelectedBaseCurrencyUseCase = setUserSelectedBaseCurrencyUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}