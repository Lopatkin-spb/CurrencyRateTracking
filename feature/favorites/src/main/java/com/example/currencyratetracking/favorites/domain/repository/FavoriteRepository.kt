package com.example.currencyratetracking.favorites.domain.repository

import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface FavoriteRepository {

    fun getListFavoritePairs(): Flow<List<CurrencyPair>>

    fun getFavoritePairs(): Flow<CurrencyPair>

    fun setPairCurrenciesToFavorite(data: CurrencyPair): Flow<Boolean>

    fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean>
}