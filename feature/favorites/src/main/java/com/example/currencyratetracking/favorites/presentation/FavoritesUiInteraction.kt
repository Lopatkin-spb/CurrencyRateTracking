package com.example.currencyratetracking.favorites.presentation

import com.example.currencyratetracking.model.CurrencyUi


data class FavoritesUiState(
    val listFavorites: List<FavoritePairCurrenciesRateUi> = emptyList(),
)


data class FavoritePairCurrenciesRateUi(
    override val id: Long,
    override val text: String,
    override val quotation: String,
    override val isFavorite: Boolean,
) : CurrencyUi(id = id, text = text, quotation = quotation, isFavorite = isFavorite)


sealed interface FavoritesUserEvent {
    data object OnScreenOpen : FavoritesUserEvent
    data object OnScreenClose : FavoritesUserEvent
    data class OnChangeFavoriteState(val currency: CurrencyUi) : FavoritesUserEvent
}