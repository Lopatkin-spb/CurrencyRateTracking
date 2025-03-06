package com.example.currencyratetracking.presentation.currencies

import com.example.currencyratetracking.presentation.CurrencyUi


internal data class CurrenciesUiState(
    val showedBaseCurrency: String = "",
    val listBaseCurrencies: List<String> = emptyList(),
    val listActualCurrencyRates: List<ActualCurrencyRateUi> = emptyList(),
)


internal data class ActualCurrencyRateUi(
    override val id: Long,
    override val name: String,
    override val charCode: String,
    override val quotation: String,
) : CurrencyUi(id = id, name = name, quotation = quotation, charCode = charCode)


sealed interface CurrenciesUserEvent {
    data object OnScreenOpen : CurrenciesUserEvent
    data object OnScreenClose : CurrenciesUserEvent
    data class OnChangeBaseCurrency(val name: String) : CurrenciesUserEvent
    data class OnSaveToFavorite(val saveState: Boolean, val currency: CurrencyUi) : CurrenciesUserEvent
}