package com.example.currencyratetracking.currencies.data.locale

import com.example.currencyratetracking.api_locale.storage.application.DatabaseApiManager
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
    private val databaseApiManager: DatabaseApiManager,
) : FavoriteLocaleDataSource, AbstractDataSource() {

    override fun savePairCurrenciesToFavorite(model: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(model)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> databaseApiManager.getFavoriteCurrencyPairApi().checkAndInsertUniquePair(dbo) }
    }

    override fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(data)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> databaseApiManager.getFavoriteCurrencyPairApi().deleteAll(dbo) }
    }

    override fun checkAvailabilityPairCurrencies(data: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(data)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> databaseApiManager.getFavoriteCurrencyPairApi().checkPairBy(dbo) }
    }
}