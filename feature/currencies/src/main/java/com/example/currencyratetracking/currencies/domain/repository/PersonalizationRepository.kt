package com.example.currencyratetracking.currencies.domain.repository

import kotlinx.coroutines.flow.Flow


interface PersonalizationRepository {

    fun getUserSelectedBaseCurrency(): Flow<String>

    fun setUserSelectedBaseCurrency(base: String): Flow<Boolean>
}