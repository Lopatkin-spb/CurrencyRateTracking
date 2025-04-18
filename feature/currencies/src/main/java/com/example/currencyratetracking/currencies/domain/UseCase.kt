package com.example.currencyratetracking.currencies.domain

import com.example.currencyratetracking.model.CurrencyActual
import com.example.currencyratetracking.model.Sorting
import kotlinx.coroutines.flow.Flow


interface GetListBaseCurrenciesUseCase {
    fun execute(): Flow<List<String>>
}


interface SetPairCurrenciesToFavoriteUseCase {
    fun execute(second: String, base: String?): Flow<Boolean>
}


interface DeletePairCurrenciesFromFavoriteByCharCodesUseCase {
    fun execute(second: String, base: String?): Flow<Boolean>
}


interface GetListActualCurrencyRatesByBaseCharCodeUseCase {
    fun execute(base: String): Flow<CurrencyActual>
}


interface GetListActualCurrencyRatesWithSortByBaseCharCodeUseCase {
    fun execute(base: String, sorting: Sorting?): Flow<CurrencyActual>
}


interface GetUserSelectedBaseCurrencyUseCase {
    fun execute(): Flow<String>
}


interface SetUserSelectedBaseCurrencyUseCase {
    fun execute(base: String): Flow<Boolean>
}