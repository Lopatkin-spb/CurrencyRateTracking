package com.example.currencyratetracking.currencies.data.remote

import com.example.currencyratetracking.api_remote.api.ApiRemoteManager
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractDataSource
import com.example.currencyratetracking.currencies.ModuleTag.TAG_LOG
import com.example.currencyratetracking.currencies.data.remote.dataSource.RateRemoteDataSource
import com.example.currencyratetracking.model.Currency
import kotlinx.coroutines.flow.*
import javax.inject.Inject


internal class RateRemoteDataSourceImpl @Inject constructor(
    private val logger: BaseLogger,
    private val apiRemoteManager: ApiRemoteManager,
) : RateRemoteDataSource, AbstractDataSource() {

    override fun getActualCurrencyRates(base: String): Flow<Currency> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(apiRemoteManager.getRatesApi().getRates(base))
        }
            .map { response -> response.rates?.getListRatesDto() }
            .filterNotNull()
            .transform { list -> emitAll(list.asFlow()) }
            .map { dto -> dto.toCurrency() }
    }

    override fun getListActualCurrencyRates(base: String): Flow<List<Currency>> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(apiRemoteManager.getRatesApi().getRates(base))
        }
            .map { response -> response.rates?.getListRatesDto() }
            .filterNotNull()
            .map { list -> list.map { it.toCurrency() } }
    }

}