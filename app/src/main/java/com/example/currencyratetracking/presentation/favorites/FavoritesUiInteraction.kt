package com.example.currencyratetracking.presentation.favorites

import com.example.currencyratetracking.presentation.CurrencyUi


internal data class FavoritesUiState(
    val listFavorites: List<FavoriteCurrencyRate> = emptyList(),
)


//TODO: correct names all
internal data class FavoriteCurrencyRate(
    override val id: Long,
    override val text: String,
    override val quotation: String,
    override val isFavorite: Boolean,
) : CurrencyUi(id = id, text = text, quotation = quotation, isFavorite = isFavorite)


internal sealed interface FavoritesUserEvent {
    data object OnScreenOpen : FavoritesUserEvent
    data object OnScreenClose : FavoritesUserEvent
    data class OnChangeFavoriteState(val currency: CurrencyUi) : FavoritesUserEvent
}