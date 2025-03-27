package com.example.currencyratetracking.data.locale

import kotlinx.coroutines.flow.Flow


interface LocaleDataSource {

    fun clearUserSession(): Flow<Boolean>
}