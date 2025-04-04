package com.example.currencyratetracking.currencies.domain.usecase

import com.example.currencyratetracking.common.DoubleRoundingConverter
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.transformToList
import com.example.currencyratetracking.currencies.domain.GetListActualCurrencyRatesWithSortByBaseCharCodeUseCase
import com.example.currencyratetracking.currencies.domain.repository.FavoriteRepository
import com.example.currencyratetracking.currencies.domain.repository.RateRepository
import com.example.currencyratetracking.currencies.domain.toCurrencyPair
import com.example.currencyratetracking.model.CurrencyActual
import com.example.currencyratetracking.model.Sorting
import kotlinx.coroutines.flow.*
import javax.inject.Inject


internal class GetListActualCurrencyRatesWithSortByBaseCharCodeUseCaseImpl @Inject constructor(
    private val rateRepository: RateRepository,
    private val favoriteRepository: FavoriteRepository,
    private val logger: BaseLogger,
    private val doubleRoundingConverter: DoubleRoundingConverter,
) : GetListActualCurrencyRatesWithSortByBaseCharCodeUseCase {

    override fun execute(base: String, sorting: Sorting?): Flow<CurrencyActual> {
        return rateRepository.getActualCurrencyRates(base)
            .filterNot { model -> model.quotation == 0.0 }
            .filterNot { model -> model.charCode.name == base }
            .map { model ->
                val rounded = doubleRoundingConverter.roundOrNull(value = model.quotation, max = 6) ?: 0.0
                model.copy(quotation = rounded)
            }
            .filterNot { model -> model.quotation == 0.0 }
            .map { model -> model.toCurrencyPair(base) }
            .flatMapConcat { model -> favoriteRepository.syncPairCurrencies(model) }
            .transformToList()
            .map { list ->
                when (sorting) {
                    Sorting.CodeAZ -> list.sortedBy { it.charCode.name }
                    Sorting.CodeZA -> list.sortedByDescending { it.charCode.name }
                    Sorting.QuoteAsc -> list.sortedBy { it.quotation }
                    Sorting.QuoteDesc -> list.sortedByDescending { it.quotation }
                    else -> list
                }
            }
            .transform { list -> emitAll(list.asFlow()) }
    }

}