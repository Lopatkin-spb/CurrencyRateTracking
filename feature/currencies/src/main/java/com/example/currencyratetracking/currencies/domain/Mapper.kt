package com.example.currencyratetracking.currencies.domain

import com.example.currencyratetracking.model.*


internal fun Currency.toCurrencyPair(baseCurrency: String): CurrencyPair {
    return CurrencyPair(
        id = this.id,
        charCodeBase = CurrencyInfo.valueOf(baseCurrency),
        charCodeSecond = this.charCode,
        quotation = this.quotation,
    )
}