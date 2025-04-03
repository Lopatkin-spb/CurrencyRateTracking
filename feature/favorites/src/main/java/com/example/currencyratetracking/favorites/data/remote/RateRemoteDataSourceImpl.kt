package com.example.currencyratetracking.favorites.data.remote

import com.example.currencyratetracking.api_remote.api.RatesApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractDataSource
import com.example.currencyratetracking.favorites.ModuleTag.TAG_LOG
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.*
import javax.inject.Inject


internal class RateRemoteDataSourceImpl @Inject constructor(
    private val logger: BaseLogger,
    private val ratesApi: RatesApi,
) : RateRemoteDataSource, AbstractDataSource() {

    override fun getActualCurrencyPairRate(pair: CurrencyPair): Flow<CurrencyPair> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(ratesApi.getRates(pair.charCodeBase.name))
        }
            .map { response -> response.rates?.getListRatesDto() }
            .filterNotNull()
            .transform { list -> emitAll(list.asFlow()) }
            .map { dto -> dto.toCurrency() }
            .filter { currency -> currency.charCode.name == pair.charCodeSecond.name }
            .map { currency -> pair.copy(quotation = currency.quotation) }
    }
}