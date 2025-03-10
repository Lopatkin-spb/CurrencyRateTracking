package com.example.currencyratetracking.currencies.presentation

import com.example.currencyratetracking.model.CurrencyUi


data class CurrenciesUiState(
    val showedBaseCurrency: String = "",
    val listBaseCurrencies: List<String> = emptyList(),
    val listActualCurrencyRates: List<ActualCurrencyRateUi> = emptyList(),
)


data class ActualCurrencyRateUi(
    override val id: Long,
    override val text: String,
    override val quotation: String,
    override val isFavorite: Boolean,
) : CurrencyUi(id = id, text = text, quotation = quotation, isFavorite = isFavorite)


sealed interface CurrenciesUserEvent {
    data object OnScreenOpen : CurrenciesUserEvent
    data object OnScreenClose : CurrenciesUserEvent
    data class OnChangeBaseCurrency(val name: String) : CurrenciesUserEvent
    data class OnChangeFavoriteState(val currency: CurrencyUi) : CurrenciesUserEvent
}