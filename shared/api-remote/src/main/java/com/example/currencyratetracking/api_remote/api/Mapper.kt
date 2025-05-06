package com.example.currencyratetracking.api_remote.api


public fun RatesDto.toListRatesDto(): List<RateDto> {
    return listOf(
        RateDto("EUR", this.eur),
        RateDto("USD", this.usd),
        RateDto("JPY", this.jpy),
        RateDto("RUB", this.rub),
        RateDto("PHP", this.php),

        RateDto("BGN", this.bgn),
        RateDto("DKK", this.dkk),
        RateDto("CZK", this.czk),
        RateDto("HUF", this.huf),
        RateDto("RON", this.ron),

        RateDto("ISK", this.isk),
        RateDto("AUD", this.aud),
        RateDto("CAD", this.cad),
        RateDto("MYR", this.myr),
        RateDto("ZAR", this.zar),
    )
}