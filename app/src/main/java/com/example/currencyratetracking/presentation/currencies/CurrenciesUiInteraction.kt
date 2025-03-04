package com.example.currencyratetracking.presentation.currencies

import com.example.currencyratetracking.presentation.Currency


internal data class ActualCurrencyPair(
    override val id: Long,
    override val name: String,
    override val quotation: Double,
) : Currency(id, name, quotation)


internal data class CurrenciesUiState(
    val listActual: List<ActualCurrencyPair> = emptyList(),
)


internal sealed interface CurrenciesUserEvent {
    data object OnScreenOpen : CurrenciesUserEvent
    data object OnScreenClose : CurrenciesUserEvent
}