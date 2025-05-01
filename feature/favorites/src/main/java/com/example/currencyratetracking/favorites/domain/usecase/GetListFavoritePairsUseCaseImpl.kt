package com.example.currencyratetracking.favorites.domain.usecase

import com.example.currencyratetracking.common.DoubleRoundingConverter
import com.example.currencyratetracking.core.domain.AbstractUseCase
import com.example.currencyratetracking.favorites.domain.GetListFavoritePairsUseCase
import com.example.currencyratetracking.favorites.domain.repository.FavoriteRepository
import com.example.currencyratetracking.favorites.domain.repository.RateRepository
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.*
import javax.inject.Inject


internal class GetListFavoritePairsUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val rateRepository: RateRepository,
    private val doubleRoundingConverter: DoubleRoundingConverter,
) : GetListFavoritePairsUseCase, AbstractUseCase() {

    override fun execute(): Flow<CurrencyPair> {
        return favoriteRepository.getFavoritePairs()
            .flatMapConcat { pair ->
                if (pair.quotation == 0.0) {
                    rateRepository.getActualCurrencyPair(pair)
                } else {
                    flowOf(pair)
                }
            }
            .onEach { actual -> favoriteRepository.updatePairCurrencies(actual).collect() }
            .map { model ->
                val rounded = doubleRoundingConverter.roundOrNull(value = model.quotation, max = 6) ?: 0.0
                model.copy(quotation = rounded)
            }
            .filterNot { pair -> pair.quotation == 0.0 }
    }
}