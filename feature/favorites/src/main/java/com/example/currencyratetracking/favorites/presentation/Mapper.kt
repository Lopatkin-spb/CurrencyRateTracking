package com.example.currencyratetracking.favorites.presentation

import com.example.currencyratetracking.model.CurrencyPair
import com.example.currencyratetracking.model.CurrencyUi


internal fun CurrencyPair.toFavoritePairCurrenciesRateUi(): FavoritePairCurrenciesRateUi {
    return FavoritePairCurrenciesRateUi(
        id = this.id,
        text = "${this.charCodeBase}/${this.charCodeSecond}",
        quotation = this.quotation.toString(),
        isFavorite = true,
    )
}

internal fun CurrencyUi.toFavoritePairCurrenciesRateUi(): FavoritePairCurrenciesRateUi {
    return FavoritePairCurrenciesRateUi(
        id = this.id,
        text = this.text,
        quotation = this.quotation,
        isFavorite = this.isFavorite,
    )
}