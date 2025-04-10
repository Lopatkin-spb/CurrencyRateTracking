package com.example.currencyratetracking.favorites.data

import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.data.AbstractRepository
import com.example.currencyratetracking.favorites.data.locale.FavoriteLocaleDataSource
import com.example.currencyratetracking.favorites.domain.repository.FavoriteRepository
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class FavoriteRepositoryImpl @Inject constructor(
    private val logger: BaseLogger,
    private val favoriteLocaleDataSource: FavoriteLocaleDataSource,
) : AbstractRepository(), FavoriteRepository {

    override fun getListFavoritePairs(): Flow<List<CurrencyPair>> {
        return favoriteLocaleDataSource.getListFavoritePairs()
    }

    override fun getFavoritePairs(): Flow<CurrencyPair> {
        return favoriteLocaleDataSource.getFavoritePairs()
    }

    override fun setPairCurrenciesToFavorite(data: CurrencyPair): Flow<Boolean> {
        return favoriteLocaleDataSource.setPairCurrenciesToFavorite(data)
    }

    override fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean> {
        return favoriteLocaleDataSource.deletePairCurrenciesFromFavorite(data)
    }
}