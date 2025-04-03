package com.example.currencyratetracking.favorites.data.locale

import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractDataSource
import com.example.currencyratetracking.favorites.ModuleTag.TAG_LOG
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.*
import javax.inject.Inject


internal class FavoriteLocaleDataSourceImpl @Inject constructor(
    private val logger: BaseLogger,
    private val favoriteCurrencyPairApi: FavoriteCurrencyPairApi,
) : AbstractDataSource(), FavoriteLocaleDataSource {

    override fun getListFavoritePairs(): Flow<List<CurrencyPair>> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(favoriteCurrencyPairApi.getAllPairs())
        }
            .map { listDbo -> listDbo.map { dbo -> dbo.toCurrencyPair() } }
    }

    override fun getFavoritePairs(): Flow<CurrencyPair> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(favoriteCurrencyPairApi.getAllPairs())
        }
            .transform { listDbo -> emitAll(listDbo.asFlow()) }
            .map { dbo -> dbo.toCurrencyPair() }
    }

    override fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(data)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> favoriteCurrencyPairApi.deleteAll(dbo) }
    }

    override fun setPairCurrenciesToFavorite(model: CurrencyPair): Flow<Boolean> {
        return flow {
            logger.v(TAG_LOG, "$NAME_FULL started")
            emit(model)
        }
            .map { it.toFavoriteCurrencyPairDbo() }
            .map { dbo -> favoriteCurrencyPairApi.checkAndInsertUniquePair(dbo) }
    }
}