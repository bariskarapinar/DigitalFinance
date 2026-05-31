package com.apexvest.core.common

import androidx.compose.runtime.Immutable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * ApexVest Precision Decimal Engine.
 * Bypasses IEEE 754 floating-point inaccuracies for financial transactions.
 */
@Immutable
data class FinanceBigDecimal(
    val value: BigDecimal
) {
    companion object {
        private const val DEFAULT_SCALE = 8
        private val ROUNDING_MODE = RoundingMode.HALF_UP

        fun fromString(amount: String): FinanceBigDecimal {
            return FinanceBigDecimal(BigDecimal(amount).setScale(DEFAULT_SCALE, ROUNDING_MODE))
        }

        fun fromDouble(amount: Double): FinanceBigDecimal {
            return FinanceBigDecimal(BigDecimal(amount.toString()).setScale(DEFAULT_SCALE, ROUNDING_MODE))
        }
        
        val ZERO = fromDouble(0.0)
    }

    operator fun plus(other: FinanceBigDecimal): FinanceBigDecimal {
        return FinanceBigDecimal(this.value.add(other.value))
    }

    operator fun minus(other: FinanceBigDecimal): FinanceBigDecimal {
        return FinanceBigDecimal(this.value.subtract(other.value))
    }

    operator fun times(other: FinanceBigDecimal): FinanceBigDecimal {
        return FinanceBigDecimal(this.value.multiply(other.value).setScale(DEFAULT_SCALE, ROUNDING_MODE))
    }

    fun toDisplayString(currencySymbol: String = "$"): String {
        return "$currencySymbol${value.setScale(2, ROUNDING_MODE)}"
    }
}
