package com.example.currencyratetracking.currencies.domain.usecase

import com.example.currencyratetracking.currencies.domain.SetPairCurrenciesToFavoriteUseCase
import com.example.currencyratetracking.currencies.domain.repository.FavoriteRepository
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


internal class SetPairCurrenciesToFavoriteUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) : SetPairCurrenciesToFavoriteUseCase {

    override fun execute(second: String, base: String?): Flow<Boolean> {
        return flow {
            val model = CurrencyPair(
                id = 0,
                charCodeBase = CurrencyInfo.valueOf(requireNotNull(base)),
                charCodeSecond = CurrencyInfo.valueOf(second),
                quotation = 0.0,
            )
            emit(model)
        }
            .flatMapMerge { data -> favoriteRepository.savePairCurrenciesToFavorite(data) }
    }

}