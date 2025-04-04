package com.example.currencyratetracking.common

import java.math.RoundingMode
import javax.inject.Inject


public interface DoubleRoundingConverter {
    public fun roundOrThrow(value: Double, max: Int): Double

    public fun roundOrNull(value: Double, max: Int): Double?
}


internal class DoubleRoundingConverterImpl @Inject constructor() : DoubleRoundingConverter {

    override fun roundOrThrow(value: Double, max: Int): Double {
        checkMaxOrThrow(max)
        val stringValue = value.toString()

        val result = if (isNeedRound(stringValue, max)) {
            stringValue.toBigDecimal().setScale(max, RoundingMode.HALF_UP).toDouble()
        } else {
            value
        }
        return result
    }

    override fun roundOrNull(value: Double, max: Int): Double? {
        if (checkMaxOrNull(max) == null) return null
        val stringValue = value.toString()

        val result = if (isNeedRound(stringValue, max)) {
            val scaled = stringValue.toBigDecimal().setScale(max, RoundingMode.HALF_UP) ?: null
            scaled?.toDouble()
        } else {
            value
        }
        return result
    }


    private fun checkMaxOrThrow(max: Int) {
        if (max < 1) throw Exception("rounding max value must greater than zero (max = $max)")
    }

    private fun checkMaxOrNull(max: Int): Boolean? {
        if (max < 1) return null
        return true
    }

    private fun isNeedRound(value: String, max: Int): Boolean {
        val lengthAfterDot = value.substringAfter(".").length

        return lengthAfterDot > max
    }
}