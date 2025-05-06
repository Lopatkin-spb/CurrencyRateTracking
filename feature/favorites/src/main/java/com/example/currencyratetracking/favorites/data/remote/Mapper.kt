package com.example.currencyratetracking.favorites.data.remote

import com.example.currencyratetracking.api_remote.api.RateDto
import com.example.currencyratetracking.model.Currency
import com.example.currencyratetracking.model.CurrencyInfo


internal fun RateDto.toCurrency(): Currency {
    return Currency(
        id = CurrencyInfo.valueOf(this.code).id.toLong(),
        charCode = CurrencyInfo.valueOf(this.code),
        quotation = this.value ?: 0.0,
    )
}