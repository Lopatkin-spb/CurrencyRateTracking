package com.example.currencyratetracking.domain.repository

import kotlinx.coroutines.flow.Flow


interface Repository {

    fun clearUserSession(): Flow<Boolean>

}