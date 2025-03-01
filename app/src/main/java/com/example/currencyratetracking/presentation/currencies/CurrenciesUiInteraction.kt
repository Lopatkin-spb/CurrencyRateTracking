package com.example.currencyratetracking.presentation.currencies


internal sealed interface CurrenciesUserEvent {
    data object OnScreenOpen : CurrenciesUserEvent
    data object OnScreenClose : CurrenciesUserEvent
}