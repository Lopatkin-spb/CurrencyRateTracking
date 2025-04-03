package com.example.currencyratetracking.favorites.domain

import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow


interface GetListFavoritePairsUseCase {
    fun execute(): Flow<CurrencyPair>
}


interface SetPairCurrenciesToFavoriteUseCase {
    fun execute(pair: String): Flow<Boolean>
}


interface DeletePairCurrenciesFromFavoriteByCharCodesUseCase {
    fun execute(pair: String): Flow<Boolean>
}