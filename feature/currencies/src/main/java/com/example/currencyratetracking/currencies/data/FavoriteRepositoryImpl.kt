package com.example.currencyratetracking.currencies.data

import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.data.AbstractRepository
import com.example.currencyratetracking.currencies.data.locale.dataSource.FavoriteLocaleDataSource
import com.example.currencyratetracking.currencies.domain.repository.FavoriteRepository
import com.example.currencyratetracking.model.CurrencyActual
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteLocaleDataSource: FavoriteLocaleDataSource,
    private val logger: BaseLogger,
) : AbstractRepository(), FavoriteRepository {

    override fun savePairCurrenciesToFavorite(data: CurrencyPair): Flow<Boolean> {
        return favoriteLocaleDataSource.savePairCurrenciesToFavorite(data)
    }

    override fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean> {
        return favoriteLocaleDataSource.deletePairCurrenciesFromFavorite(data)
    }

    override fun syncPairCurrencies(data: CurrencyPair): Flow<CurrencyActual> {
        return favoriteLocaleDataSource.checkAvailabilityPairCurrencies(data)
            .map { favoriteState -> data.toCurrencyActual(favoriteState) }
    }
}