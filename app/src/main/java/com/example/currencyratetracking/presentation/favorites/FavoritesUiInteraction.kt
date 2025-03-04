package com.example.currencyratetracking.presentation.favorites

import com.example.currencyratetracking.presentation.Currency
import com.example.currencyratetracking.presentation.UserEvent


//TODO: correct names all
internal data class FavoriteCurrencyPair(
    override val id: Long,
    override val name: String,
    override val quotation: Double,
) : Currency(id, name, quotation)


internal data class FavoritesUiState(
    val listFavorites: List<FavoriteCurrencyPair> = emptyList(),
)


internal sealed interface FavoritesUserEvent : UserEvent {
    data object OnScreenOpen : FavoritesUserEvent
    data object OnScreenClose : FavoritesUserEvent
}