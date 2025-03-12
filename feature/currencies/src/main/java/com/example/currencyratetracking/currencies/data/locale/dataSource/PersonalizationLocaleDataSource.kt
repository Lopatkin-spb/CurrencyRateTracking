package com.example.currencyratetracking.currencies.data.locale.dataSource

import kotlinx.coroutines.flow.Flow


interface PersonalizationLocaleDataSource {

    fun getUserSelectedBaseCurrency(): Flow<String>

    fun setUserSelectedBaseCurrency(base: String): Flow<Boolean>
}