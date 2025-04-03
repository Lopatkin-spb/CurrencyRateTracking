package com.example.currencyratetracking.favorites.data.locale

import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface FavoriteLocaleDataSource {

    fun getListFavoritePairs(): Flow<List<CurrencyPair>>

    fun getFavoritePairs(): Flow<CurrencyPair>

    fun deletePairCurrenciesFromFavorite(data: CurrencyPair): Flow<Boolean>

    fun setPairCurrenciesToFavorite(model: CurrencyPair): Flow<Boolean>

}