package com.example.currencyratetracking.model


public open class CurrencyUi(
    public open val id: Long,
    public open val text: String,
    public open val quotation: String,
    public open val isFavorite: Boolean,
)