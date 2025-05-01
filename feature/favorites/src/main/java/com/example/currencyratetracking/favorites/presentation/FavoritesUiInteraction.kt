package com.example.currencyratetracking.favorites.presentation

import com.example.currencyratetracking.core.presentation.UiEvent
import com.example.currencyratetracking.model.CurrencyUi


sealed class FavoritesUiState {
    data object Empty : FavoritesUiState()
    data object Loading : FavoritesUiState()
    data class Success(val listFavorites: List<FavoritePairCurrenciesRateUi> = emptyList()) : FavoritesUiState()
    data class Error(val text: String) : FavoritesUiState()

    fun asSuccess(): Success {
        return checkNotNull(this as? Success)
    }
}


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