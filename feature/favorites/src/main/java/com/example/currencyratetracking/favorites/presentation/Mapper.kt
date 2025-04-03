package com.example.currencyratetracking.favorites.presentation

import com.example.currencyratetracking.model.CurrencyPair


internal fun CurrencyPair.toFavoritePairCurrenciesRateUi(): FavoritePairCurrenciesRateUi {
    return FavoritePairCurrenciesRateUi(
        id = this.id,
        text = "${this.charCodeBase}/${this.charCodeSecond}",
        quotation = this.quotation.toString(),
        isFavorite = true,
    )
}