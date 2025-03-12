package com.example.currencyratetracking.currencies.data

import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractRepository
import com.example.currencyratetracking.currencies.data.remote.dataSource.RateRemoteDataSource
import com.example.currencyratetracking.currencies.domain.repository.RateRepository
import com.example.currencyratetracking.model.Currency
import com.example.currencyratetracking.model.CurrencyInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


internal class RateRepositoryImpl @Inject constructor(
    private val logger: BaseLogger,
    private val rateRemoteDataSource: RateRemoteDataSource,
) : AbstractRepository(), RateRepository {

    override fun getListBaseCurrencies(): Flow<List<String>> {
        return flow {
            val listStub = mutableListOf<String>()
            val enumEntries = CurrencyInfo.entries
            for (index in 0 until enumEntries.size - 7) {
                val name = enumEntries[index].toString()
                listStub.add(name)
            }
            emit(listStub)
        }
    }

    override fun getListActualCurrencyRates(base: String): Flow<List<Currency>> {
        return rateRemoteDataSource.getListActualCurrencyRates(base)
    }

    override fun getActualCurrencyRates(base: String): Flow<Currency> {
        return rateRemoteDataSource.getActualCurrencyRates(base)
    }

}