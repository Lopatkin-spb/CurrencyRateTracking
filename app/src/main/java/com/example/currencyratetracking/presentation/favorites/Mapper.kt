package com.example.currencyratetracking.presentation.favorites

import com.example.currencyratetracking.model.CurrencyPair


internal fun CurrencyPair.toFavoriteCurrencyRate(): FavoriteCurrencyRate {
    return FavoriteCurrencyRate(
        id = this.id,
        text = "${this.charCodeBase}/${this.charCodeSecond}",
        quotation = this.quotation.toString(),
        isFavorite = true,
    )
}