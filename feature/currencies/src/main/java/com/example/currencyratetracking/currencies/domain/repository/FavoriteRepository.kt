package com.example.currencyratetracking.currencies.domain.repository

import com.example.currencyratetracking.model.CurrencyActual
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface FavoriteRepository {

    fun savePairCurrenciesToFavorite(data: CurrencyPair): Flow<Boolean>

    fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean>

    fun syncPairCurrencies(data: CurrencyPair): Flow<CurrencyActual>

}