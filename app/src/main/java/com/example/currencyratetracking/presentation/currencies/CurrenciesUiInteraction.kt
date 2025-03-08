package com.example.currencyratetracking.presentation.currencies

import com.example.currencyratetracking.presentation.CurrencyUi
import com.example.currencyratetracking.presentation.UserEvent


internal data class CurrenciesUiState(
    val showedBaseCurrency: String = "",
    val listBaseCurrencies: List<String> = emptyList(),
    val listActualCurrencyRates: List<ActualCurrencyRateUi> = emptyList(),
)


internal data class ActualCurrencyRateUi(
    override val id: Long,
    override val text: String,
    override val quotation: String,
    override val isFavorite: Boolean,
) : CurrencyUi(id = id, text = text, quotation = quotation, isFavorite = isFavorite)


sealed interface CurrenciesUserEvent : UserEvent {
    data object OnScreenOpen : CurrenciesUserEvent
    data object OnScreenClose : CurrenciesUserEvent
    data class OnChangeBaseCurrency(val name: String) : CurrenciesUserEvent
    data class OnChangeFavoriteState(val currency: CurrencyUi) : CurrenciesUserEvent
}