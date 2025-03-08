package com.example.currencyratetracking.model


public open class Currency(
    public open val id: Long,
    public open val charCode: CurrencyInfo,
    public open val quotation: Double,
)


public open class CurrencyPair(
    public open val id: Long,
    public open val charCodeBase: CurrencyInfo,
    public open val charCodeSecond: CurrencyInfo,
    public open val quotation: Double,
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