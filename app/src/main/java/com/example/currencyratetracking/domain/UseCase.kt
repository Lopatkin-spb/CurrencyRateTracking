package com.example.currencyratetracking.domain

import kotlinx.coroutines.flow.Flow


interface ClearUserSessionByLiveCycleUseCase {
    fun execute(): Flow<Boolean>
}