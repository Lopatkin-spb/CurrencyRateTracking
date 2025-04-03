package com.example.currencyratetracking.favorites.data

import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractRepository
import com.example.currencyratetracking.favorites.data.remote.RateRemoteDataSource
import com.example.currencyratetracking.favorites.domain.repository.RateRepository
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class RateRepositoryImpl @Inject constructor(
    private val logger: BaseLogger,
    private val rateRemoteDataSource: RateRemoteDataSource,
) : RateRepository, AbstractRepository() {

    override fun getActualCurrencyPair(pair: CurrencyPair): Flow<CurrencyPair> {
        return rateRemoteDataSource.getActualCurrencyPairRate(pair)
    }
}