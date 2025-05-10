package com.example.currencyratetracking.data.locale

import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_locale.api.rate.CurrencyPairRateApi
import com.example.currencyratetracking.common_android.AppTag
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.Tag
import com.example.currencyratetracking.core.data.AbstractDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


internal class LocaleDataSourceImpl @Inject constructor(
    private val currencyPairRateApi: CurrencyPairRateApi,
    private val favoriteCurrencyPairApi: FavoriteCurrencyPairApi,
    private val logger: BaseLogger,
    @AppTag private val tag: Tag,
) : AbstractDataSource(), LocaleDataSource {

    override fun clearUserSession(): Flow<Boolean> {
        return flow {
            val items = currencyPairRateApi.clean()
            logger.v(tag.LOG, "$NAME_FULL items deleted = $items")
            emit(items > 0)

            val itemsQ = favoriteCurrencyPairApi.cleanQuotations()
            logger.v(tag.LOG, "$NAME_FULL items cleaned = $itemsQ")
            emit(itemsQ > 0)
        }
    }
}