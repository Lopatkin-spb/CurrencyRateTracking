package com.example.currencyratetracking.presentation.favorites

import com.example.currencyratetracking.presentation.CurrencyUi
import com.example.currencyratetracking.presentation.UserEvent


//TODO: correct names all
internal data class FavoriteCurrencyRate(
    override val id: Long,
    override val name: String,
    override val charCode: String,
    override val quotation: String,
) : CurrencyUi(id = id, name = name, quotation = quotation, charCode = charCode)


internal data class FavoritesUiState(
    val listFavorites: List<FavoriteCurrencyRate> = emptyList(),
)


internal sealed interface FavoritesUserEvent : UserEvent {
    data object OnScreenOpen : FavoritesUserEvent
    data object OnScreenClose : FavoritesUserEvent
}