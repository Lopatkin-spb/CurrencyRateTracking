package com.example.currencyratetracking.favorites.data.locale

import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairDbo
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.model.CurrencyPair


internal fun FavoriteCurrencyPairDbo.toCurrencyPair(): CurrencyPair {
    return CurrencyPair(
        id = this.id,
        charCodeBase = CurrencyInfo.valueOf(this.charCodeBase),
        charCodeSecond = CurrencyInfo.valueOf(this.charCodeSecond),
        quotation = this.quotation,
    )
}

internal fun CurrencyPair.toFavoriteCurrencyPairDbo(): FavoriteCurrencyPairDbo {
    return FavoriteCurrencyPairDbo(
        id = this.id,
        charCodeBase = this.charCodeBase.name,
        charCodeSecond = this.charCodeSecond.name,
        quotation = this.quotation,
    )
}