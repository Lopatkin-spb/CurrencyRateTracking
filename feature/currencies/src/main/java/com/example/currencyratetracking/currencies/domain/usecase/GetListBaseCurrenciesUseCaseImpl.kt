package com.example.currencyratetracking.currencies.domain.usecase

import com.example.currencyratetracking.currencies.domain.GetListBaseCurrenciesUseCase
import com.example.currencyratetracking.currencies.domain.repository.RateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class GetListBaseCurrenciesUseCaseImpl @Inject constructor(
    private val rateRepository: RateRepository,
) : GetListBaseCurrenciesUseCase {

    override fun execute(): Flow<List<String>> {
        return rateRepository.getListBaseCurrencies()
    }

}