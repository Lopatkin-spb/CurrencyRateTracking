package com.example.currencyratetracking.currencies.presentation

import com.example.currencyratetracking.model.CurrencyActual
import com.example.currencyratetracking.model.CurrencyUi


internal fun CurrencyActual.toActualCurrencyRateUi(): ActualCurrencyRateUi {
    return ActualCurrencyRateUi(
        id = this.id,
        text = this.charCode.name,
        quotation = this.quotation.toString(),
        isFavorite = this.isFavorite,
    )
}


internal fun CurrencyUi.toActualCurrencyRateUi(): ActualCurrencyRateUi {
    return ActualCurrencyRateUi(
        id = this.id,
        text = this.text,
        quotation = this.quotation,
        isFavorite = this.isFavorite,
    )
}