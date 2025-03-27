package com.example.currencyratetracking.currencies.data.locale.dataSource

import com.example.currencyratetracking.model.Currency
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface RateLocaleDataSource {

    fun saveActualCurrencyRate(data: CurrencyPair): Flow<Boolean>

    fun getActualCurrencyRates(baseCurrency: String): Flow<Currency>
}