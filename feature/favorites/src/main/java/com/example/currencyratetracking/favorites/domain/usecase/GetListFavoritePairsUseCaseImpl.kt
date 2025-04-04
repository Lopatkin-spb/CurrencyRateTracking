package com.example.currencyratetracking.favorites.domain.usecase

import com.example.currencyratetracking.common.DoubleRoundingConverter
import com.example.currencyratetracking.favorites.domain.GetListFavoritePairsUseCase
import com.example.currencyratetracking.favorites.domain.repository.FavoriteRepository
import com.example.currencyratetracking.favorites.domain.repository.RateRepository
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal class GetListFavoritePairsUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val rateRepository: RateRepository,
    private val doubleRoundingConverter: DoubleRoundingConverter,
) : GetListFavoritePairsUseCase {

    override fun execute(): Flow<CurrencyPair> {
        return favoriteRepository.getFavoritePairs()
            .flatMapConcat { pair -> rateRepository.getActualCurrencyPair(pair) }
            .map { model ->
                val rounded = doubleRoundingConverter.round(value = model.quotation, scale = 6)
                model.copy(quotation = rounded)
            }
    }
}