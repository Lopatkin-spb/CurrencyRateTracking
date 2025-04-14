package com.example.currencyratetracking.favorites.presentation

import com.example.currencyratetracking.core.presentation.UiEvent
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


sealed interface FavoritesUiEvent : UiEvent {
    data object OnScreenOpen : FavoritesUiEvent
    data object OnScreenClose : FavoritesUiEvent
    data class OnChangeFavoriteState(val currency: CurrencyUi) : FavoritesUiEvent
}