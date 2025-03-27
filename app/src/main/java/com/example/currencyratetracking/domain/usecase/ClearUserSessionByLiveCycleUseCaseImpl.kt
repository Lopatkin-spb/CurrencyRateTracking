package com.example.currencyratetracking.domain.usecase

import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class ClearUserSessionByLiveCycleUseCaseImpl @Inject constructor(
    private val repository: Repository,
) : ClearUserSessionByLiveCycleUseCase {

    override fun execute(): Flow<Boolean> {
        return repository.clearUserSession()
    }
}