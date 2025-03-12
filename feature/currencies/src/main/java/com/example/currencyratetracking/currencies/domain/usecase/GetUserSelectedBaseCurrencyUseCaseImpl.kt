package com.example.currencyratetracking.currencies.domain.usecase

import com.example.currencyratetracking.currencies.domain.GetUserSelectedBaseCurrencyUseCase
import com.example.currencyratetracking.currencies.domain.repository.PersonalizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject


internal class GetUserSelectedBaseCurrencyUseCaseImpl @Inject constructor(
    private val personalizationRepository: PersonalizationRepository,
) : GetUserSelectedBaseCurrencyUseCase {

    override fun execute(): Flow<String> {
        return personalizationRepository.getUserSelectedBaseCurrency()
            .filterNot { it.isEmpty() }
            .onEmpty { emit("USD") }
    }
}