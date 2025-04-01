package com.example.currencyratetracking.currencies.data.locale

import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractDataSource
import com.example.currencyratetracking.currencies.ModuleTag.TAG_LOG
import com.example.currencyratetracking.currencies.data.locale.dataSource.FavoriteLocaleDataSource
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal class FavoriteLocaleDataSourceImpl @Inject constructor(
    private val logger: BaseLogger,
    private val favoriteCurrencyPairApi: FavoriteCurrencyPairApi,
) : FavoriteLocaleDataSource, AbstractDataSource() {

    override fun savePairCurrenciesToFavorite(model: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(model)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> favoriteCurrencyPairApi.checkAndInsertUniquePair(dbo) }
    }

    override fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(data)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> favoriteCurrencyPairApi.deleteAll(dbo) }
    }

    override fun checkAvailabilityPairCurrencies(data: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(data)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> favoriteCurrencyPairApi.checkPairBy(dbo) }
    }
}