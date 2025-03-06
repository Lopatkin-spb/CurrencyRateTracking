package com.example.currencyratetracking.presentation

import com.example.currencyratetracking.api_remote.api.RateDto
import com.example.currencyratetracking.model.Currency


fun RateDto.toCurrency(): Currency {
    return Currency(
        id = this.code.id.toLong(),
        charCode = this.code,
        quotation = this.value ?: 0.0,
    )
}