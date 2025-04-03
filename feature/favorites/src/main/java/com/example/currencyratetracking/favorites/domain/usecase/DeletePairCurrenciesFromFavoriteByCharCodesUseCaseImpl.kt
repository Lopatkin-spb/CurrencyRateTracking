package com.example.currencyratetracking.favorites.domain.usecase

import com.example.currencyratetracking.api_locale.storage.application.ResourceManager
import com.example.currencyratetracking.favorites.R
import com.example.currencyratetracking.favorites.domain.DeletePairCurrenciesFromFavoriteByCharCodesUseCase
import com.example.currencyratetracking.favorites.domain.repository.FavoriteRepository
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.model.CurrencyPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


internal class DeletePairCurrenciesFromFavoriteByCharCodesUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val resourceManager: ResourceManager,
) : DeletePairCurrenciesFromFavoriteByCharCodesUseCase {

    override fun execute(pair: String): Flow<Boolean> {
        return flow {
            val delimiter = resourceManager.getString(R.string.text_pair_delimiter)

            val model = CurrencyPair(
                id = 0,
                charCodeBase = CurrencyInfo.valueOf(pair.substringBefore(delimiter)),
                charCodeSecond = CurrencyInfo.valueOf(pair.substringAfter(delimiter)),
                quotation = 0.0,
            )
            emit(model)
        }
            .flatMapMerge { data -> favoriteRepository.deletePairCurrenciesFromFavorite(data) }
    }

}