package com.example.currencyratetracking.currencies.data.remote.dataSource

import com.example.currencyratetracking.model.Currency
import kotlinx.coroutines.flow.Flow


interface RateRemoteDataSource {

    fun getListActualCurrencyRates(base: String): Flow<List<Currency>>

    fun getActualCurrencyRates(base: String): Flow<Currency>
}