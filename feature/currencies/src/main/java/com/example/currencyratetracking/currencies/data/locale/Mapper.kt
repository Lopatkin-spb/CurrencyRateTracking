package com.example.currencyratetracking.currencies.data.locale

import com.example.currencyratetracking.api_locale.api.FavoriteCurrencyPairDbo
import com.example.currencyratetracking.model.CurrencyPair


internal fun CurrencyPair.toFavoriteCurrencyPairDbo(): FavoriteCurrencyPairDbo {
    return FavoriteCurrencyPairDbo(
        id = this.id,
        charCodeBase = this.charCodeBase.name,
        charCodeSecond = this.charCodeSecond.name,
        quotation = this.quotation,
    )
}