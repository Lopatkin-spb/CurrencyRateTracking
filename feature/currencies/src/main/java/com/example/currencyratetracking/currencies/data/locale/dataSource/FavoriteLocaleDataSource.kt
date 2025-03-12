package com.example.currencyratetracking.currencies.data.locale.dataSource

import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface FavoriteLocaleDataSource {

    fun savePairCurrenciesToFavorite(model: CurrencyPair): Flow<Boolean>

    fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean>

    fun checkAvailabilityPairCurrencies(data: CurrencyPair): Flow<Boolean>
}