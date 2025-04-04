package com.example.currencyratetracking.common

import java.math.RoundingMode
import javax.inject.Inject


public interface DoubleRoundingConverter {
    public fun round(value: Double, scale: Int): Double
}


internal class DoubleRoundingConverterImpl @Inject constructor() : DoubleRoundingConverter {

    override fun round(value: Double, scale: Int): Double {
        val rounded = value.toBigDecimal().setScale(scale, RoundingMode.HALF_UP).toDouble()
        return rounded
    }

}