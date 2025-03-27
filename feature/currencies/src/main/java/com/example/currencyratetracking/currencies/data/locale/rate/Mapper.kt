package com.example.currencyratetracking.currencies.data.locale.rate

import com.example.currencyratetracking.api_locale.api.rate.CurrencyPairRateDbo
import com.example.currencyratetracking.model.Currency
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.model.CurrencyPair


internal fun Currency.toCurrencyPair(baseCurrency: String): CurrencyPair {
    return CurrencyPair(
        id = this.id,
        charCodeBase = CurrencyInfo.valueOf(baseCurrency),
        charCodeSecond = this.charCode,
        quotation = this.quotation,
    )
}

internal fun CurrencyPair.toCurrency(): Currency {
    return Currency(
        id = this.id,
        charCode = this.charCodeSecond,
        quotation = this.quotation,
    )
}

internal fun CurrencyPair.toCurrencyPairRateDbo(): CurrencyPairRateDbo {
    return CurrencyPairRateDbo(
        id = 0,
        charCodeBase = this.charCodeBase.name,
        charCodeSecond = this.charCodeSecond.name,
        quotation = this.quotation,
    )
}

internal fun CurrencyPairRateDbo.toCurrency(): Currency {
    return Currency(
        id = this.id,
        charCode = CurrencyInfo.valueOf(this.charCodeSecond),
        quotation = this.quotation,
    )
}