package com.example.currencyratetracking.api_remote.api

import com.example.currencyratetracking.model.CurrencyInfo
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
    @SerialName("EUR") val valueEUR: Double? = null,
    @SerialName("USD") val valueUSD: Double? = null,
    @SerialName("JPY") val valueJPY: Double? = null,
    @SerialName("RUB") val valueRUB: Double? = null,
    @SerialName("PHP") val valuePHP: Double? = null,
    @SerialName("BGN") val valueBGN: Double? = null,
    @SerialName("DKK") val valueDKK: Double? = null,
    @SerialName("CZK") val valueCZK: Double? = null,
    @SerialName("HUF") val valueHUF: Double? = null,
    @SerialName("RON") val valueRON: Double? = null,
    @SerialName("ISK") val valueISK: Double? = null,
    @SerialName("AUD") val valueAUD: Double? = null,
    @SerialName("CAD") val valueCAD: Double? = null,
    @SerialName("MYR") val valueMYR: Double? = null,
    @SerialName("ZAR") val valueZAR: Double? = null,
) {
    private fun getRatePHP(): RateDto = RateDto(CurrencyInfo.PHP, valuePHP)
    private fun getRateRUB(): RateDto = RateDto(CurrencyInfo.RUB, valueRUB)
    private fun getRateJPY(): RateDto = RateDto(CurrencyInfo.JPY, valueJPY)
    private fun getRateUSD(): RateDto = RateDto(CurrencyInfo.USD, valueUSD)
    private fun getRateEUR(): RateDto = RateDto(CurrencyInfo.EUR, valueEUR)
    private fun getRateBGN(): RateDto = RateDto(CurrencyInfo.BGN, valueBGN)
    private fun getRateDKK(): RateDto = RateDto(CurrencyInfo.DKK, valueDKK)
    private fun getRateCZK(): RateDto = RateDto(CurrencyInfo.CZK, valueCZK)
    private fun getRateHUF(): RateDto = RateDto(CurrencyInfo.HUF, valueHUF)
    private fun getRateRON(): RateDto = RateDto(CurrencyInfo.RON, valueRON)
    private fun getRateISK(): RateDto = RateDto(CurrencyInfo.ISK, valueISK)
    private fun getRateAUD(): RateDto = RateDto(CurrencyInfo.AUD, valueAUD)
    private fun getRateCAD(): RateDto = RateDto(CurrencyInfo.CAD, valueCAD)
    private fun getRateMYR(): RateDto = RateDto(CurrencyInfo.MYR, valueMYR)
    private fun getRateZAR(): RateDto = RateDto(CurrencyInfo.ZAR, valueZAR)

    public fun getListRatesDto(): List<RateDto> {
        return listOf(
            getRatePHP(), getRateRUB(), getRateJPY(), getRateUSD(), getRateEUR(),
            getRateBGN(), getRateDKK(), getRateCZK(), getRateHUF(), getRateRON(), getRateISK(),
            getRateAUD(), getRateCAD(), getRateMYR(), getRateZAR()
        )
    }
}

public data class RateDto(
    val code: CurrencyInfo,
    val value: Double?,
)