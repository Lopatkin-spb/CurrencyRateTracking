package com.example.currencyratetracking.currencies.domain.repository

import com.example.currencyratetracking.model.Currency
import kotlinx.coroutines.flow.Flow


interface RateRepository {

    fun getListBaseCurrencies(): Flow<List<String>>

    fun getListActualCurrencyRates(base: String): Flow<List<Currency>>

    fun getActualCurrencyRates(base: String): Flow<Currency>
}