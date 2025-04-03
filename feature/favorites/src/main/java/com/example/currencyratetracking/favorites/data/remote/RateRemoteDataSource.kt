package com.example.currencyratetracking.favorites.data.remote

import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface RateRemoteDataSource {

    fun getActualCurrencyPairRate(pair: CurrencyPair): Flow<CurrencyPair>
}