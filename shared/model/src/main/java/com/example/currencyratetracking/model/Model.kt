package com.example.currencyratetracking.model


public data class Currency(
    public val id: Long,
    public val charCode: CurrencyInfo,
    public val quotation: Double,
)


public data class CurrencyActual(
    public val id: Long,
    public val charCode: CurrencyInfo,
    public val quotation: Double,
    public val isFavorite: Boolean,
)


public data class CurrencyPair(
    public val id: Long,
    public val charCodeBase: CurrencyInfo,
    public val charCodeSecond: CurrencyInfo,
    public val quotation: Double,
)


public enum class CurrencyInfo(
    public val id: Int,
) {
    EUR(1),
    USD(2),
    JPY(3),
    RUB(4),
    PHP(5),
    BGN(6),
    DKK(7),
    CZK(8),
    HUF(9),
    RON(10),
    ISK(11),
    AUD(12),
    CAD(13),
    MYR(14),
    ZAR(15),
}