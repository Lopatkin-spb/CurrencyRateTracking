package com.example.currencyratetracking.presentation.currencies

import com.example.currencyratetracking.model.Currency


//TODO: bug if value < 6 char from dot then added zero
internal fun Currency.toActualCurrencyRateUi(): ActualCurrencyRateUi {
    val quotation = String.format("%.6f", this.quotation)

    return ActualCurrencyRateUi(
        id = this.id,
        text = this.charCode.name,
        quotation = quotation,
        isFavorite = false,
    )
}