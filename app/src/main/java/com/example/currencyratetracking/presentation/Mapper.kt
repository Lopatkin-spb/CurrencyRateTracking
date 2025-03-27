package com.example.currencyratetracking.presentation

import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairDbo
import com.example.currencyratetracking.api_remote.api.RateDto
import com.example.currencyratetracking.model.Currency
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.model.CurrencyPair
import com.example.currencyratetracking.model.CurrencyUi


fun RateDto.toCurrency(): Currency {
    return Currency(
        id = this.code.id.toLong(),
        charCode = this.code,
        quotation = this.value ?: 0.0,
    )
}

/**
 * from currency ui to pair model
 */
fun CurrencyUi.toCurrencyPair(baseCurrency: String): CurrencyPair {
    return CurrencyPair(
        id = 0,
        charCodeBase = CurrencyInfo.valueOf(baseCurrency),
        charCodeSecond = CurrencyInfo.valueOf(this.text),
        quotation = this.quotation.toDouble(),
    )
}

/**
 * from favorite pair ui to pair model
 */
fun CurrencyUi.toCurrencyPair(): CurrencyPair {
    val charCodeBase = this.text.substringBefore("/")
    val charCodeSecond = this.text.substringAfter("/")

    return CurrencyPair(
        id = this.id,
        charCodeBase = CurrencyInfo.valueOf(charCodeBase),
        charCodeSecond = CurrencyInfo.valueOf(charCodeSecond),
        quotation = this.quotation.toDouble(),
    )
}

fun CurrencyPair.toFavoriteCurrencyPairDbo(): FavoriteCurrencyPairDbo {
    return FavoriteCurrencyPairDbo(
        id = this.id,
        charCodeBase = this.charCodeBase.name,
        charCodeSecond = this.charCodeSecond.name,
        quotation = this.quotation,
    )
}


internal fun FavoriteCurrencyPairDbo.toCurrencyPair(): CurrencyPair {
    return CurrencyPair(
        id = this.id,
        charCodeBase = CurrencyInfo.valueOf(this.charCodeBase),
        charCodeSecond = CurrencyInfo.valueOf(this.charCodeSecond),
        quotation = this.quotation,
    )
}

internal fun FavoriteCurrencyPairDbo.toCurrency(): Currency {
    return Currency(
        id = this.id,
        charCode = CurrencyInfo.valueOf(this.charCodeBase),
        quotation = this.quotation,
    )
}