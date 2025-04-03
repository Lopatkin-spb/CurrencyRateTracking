package com.example.currencyratetracking.favorites.domain.repository

import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface RateRepository {

    fun getActualCurrencyPair(pair: CurrencyPair): Flow<CurrencyPair>

}