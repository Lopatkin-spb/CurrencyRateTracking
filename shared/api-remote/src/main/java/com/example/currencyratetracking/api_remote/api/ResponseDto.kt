package com.example.currencyratetracking.api_remote.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
public data class ResponseDto(
    @SerialName("date") val date: String? = null,
    @SerialName("base") val base: String? = null,
    @SerialName("rates") val rates: RatesDto? = null,
)

@Serializable
public data class RatesDto(
    @SerialName("EUR") val eur: Double? = null,
    @SerialName("USD") val usd: Double? = null,
    @SerialName("JPY") val jpy: Double? = null,
    @SerialName("RUB") val rub: Double? = null,
    @SerialName("PHP") val php: Double? = null,
    @SerialName("BGN") val bgn: Double? = null,
    @SerialName("DKK") val dkk: Double? = null,
    @SerialName("CZK") val czk: Double? = null,
    @SerialName("HUF") val huf: Double? = null,
    @SerialName("RON") val ron: Double? = null,
    @SerialName("ISK") val isk: Double? = null,
    @SerialName("AUD") val aud: Double? = null,
    @SerialName("CAD") val cad: Double? = null,
    @SerialName("MYR") val myr: Double? = null,
    @SerialName("ZAR") val zar: Double? = null,
)

public data class RateDto(
    val code: String,
    val value: Double?,
)