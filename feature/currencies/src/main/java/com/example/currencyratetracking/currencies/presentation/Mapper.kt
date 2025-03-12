package com.example.currencyratetracking.currencies.presentation

import com.example.currencyratetracking.model.CurrencyActual


internal fun CurrencyActual.toActualCurrencyRateUi(): ActualCurrencyRateUi {
    return ActualCurrencyRateUi(
        id = this.id,
        text = this.charCode.name,
        quotation = this.quotation.toString(),
        isFavorite = this.isFavorite,
    )
}