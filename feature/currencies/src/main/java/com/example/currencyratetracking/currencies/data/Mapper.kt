package com.example.currencyratetracking.currencies.data

import com.example.currencyratetracking.model.CurrencyActual
import com.example.currencyratetracking.model.CurrencyPair


internal fun CurrencyPair.toCurrencyActual(state: Boolean): CurrencyActual {
    return CurrencyActual(
        id = this.id,
        charCode = this.charCodeSecond,
        quotation = this.quotation,
        isFavorite = state,
    )
}