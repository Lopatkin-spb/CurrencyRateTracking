package com.example.currencyratetracking.currencies.domain.usecase

import com.example.currencyratetracking.currencies.domain.SetUserSelectedBaseCurrencyUseCase
import com.example.currencyratetracking.currencies.domain.repository.PersonalizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class SetUserSelectedBaseCurrencyUseCaseImpl @Inject constructor(
    private val personalizationRepository: PersonalizationRepository,
) : SetUserSelectedBaseCurrencyUseCase {

    override fun execute(base: String): Flow<Boolean> {
        return personalizationRepository.setUserSelectedBaseCurrency(base)
    }
}