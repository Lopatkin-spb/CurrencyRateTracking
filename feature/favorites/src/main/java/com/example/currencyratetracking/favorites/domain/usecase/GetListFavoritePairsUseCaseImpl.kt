package com.example.currencyratetracking.favorites.domain.usecase

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
) : GetListFavoritePairsUseCase {

    override fun execute(): Flow<CurrencyPair> {
        return favoriteRepository.getFavoritePairs()
            .flatMapConcat { pair -> rateRepository.getActualCurrencyPair(pair) }
            .map { model ->
                //TODO: maybe round
                val quotationSix = String.format("%.6f", model.quotation)

                model.copy(quotation = quotationSix.toDouble())
            }
    }
}