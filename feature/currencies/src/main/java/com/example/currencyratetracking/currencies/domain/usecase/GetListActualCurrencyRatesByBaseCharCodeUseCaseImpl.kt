package com.example.currencyratetracking.currencies.domain.usecase

import com.example.currencyratetracking.common.DoubleRoundingConverter
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.currencies.domain.GetListActualCurrencyRatesByBaseCharCodeUseCase
import com.example.currencyratetracking.currencies.domain.repository.FavoriteRepository
import com.example.currencyratetracking.currencies.domain.repository.RateRepository
import com.example.currencyratetracking.currencies.domain.toCurrencyPair
import com.example.currencyratetracking.model.CurrencyActual
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal class GetListActualCurrencyRatesByBaseCharCodeUseCaseImpl @Inject constructor(
    private val rateRepository: RateRepository,
    private val favoriteReposotory: FavoriteRepository,
    private val logger: BaseLogger,
    private val doubleRoundingConverter: DoubleRoundingConverter,
) : GetListActualCurrencyRatesByBaseCharCodeUseCase {

    override fun execute(base: String): Flow<CurrencyActual> {
        return rateRepository.getActualCurrencyRates(base)
            .filterNot { model -> model.quotation == 0.0 }
            .filterNot { model -> model.charCode.name == base }
            .map { model ->
                val rounded = doubleRoundingConverter.roundOrNull(value = model.quotation, max = 6) ?: 0.0
                model.copy(quotation = rounded)
            }
            .filterNot { model -> model.quotation == 0.0 }
            .map { model -> model.toCurrencyPair(base) }
            .flatMapConcat { model -> favoriteReposotory.syncPairCurrencies(model) }
    }

}