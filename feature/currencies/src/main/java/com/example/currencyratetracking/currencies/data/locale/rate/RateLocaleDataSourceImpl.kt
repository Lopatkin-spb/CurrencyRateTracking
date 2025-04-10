package com.example.currencyratetracking.currencies.data.locale.rate

import com.example.currencyratetracking.api_locale.api.rate.CurrencyPairRateApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.data.AbstractDataSource
import com.example.currencyratetracking.currencies.ModuleTag.TAG_LOG
import com.example.currencyratetracking.currencies.data.locale.dataSource.RateLocaleDataSource
import com.example.currencyratetracking.model.Currency
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.*
import javax.inject.Inject


internal class RateLocaleDataSourceImpl @Inject constructor(
    private val logger: BaseLogger,
    private val currencyPairRateApi: CurrencyPairRateApi,
) : RateLocaleDataSource, AbstractDataSource() {

    override fun saveActualCurrencyRate(data: CurrencyPair): Flow<Boolean> {
        return flow { emit(data) }
            .map { it.toCurrencyPairRateDbo() }
            .map {
                val result = currencyPairRateApi.insertPair(it)
                if (result == 0L) logger.v(TAG_LOG, "$NAME_FULL dont saved data = $data")
                result > 0
            }
    }

    override fun getActualCurrencyRates(baseCurrency: String): Flow<Currency> {
        return flow { emit(baseCurrency) }
            .map { currencyPairRateApi.getAllPairsBy(it) }
            .transform { emitAll(it.asFlow()) }
            .map { it.toCurrency() }
    }

}