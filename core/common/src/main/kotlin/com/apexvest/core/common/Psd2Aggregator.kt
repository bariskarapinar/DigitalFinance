package com.apexvest.core.common

/**
 * ApexVest PSD2 Mapping Layer.
 * Standardizes ISO 20022 formats from external institutions.
 */
interface ExternalAccount {
    val institutionId: String
    val iban: String
    val balance: FinanceBigDecimal
}

data class InstitutionPayload(
    val rawId: String,
    val iso20022Format: String,
    val amount: Double
)

object Psd2Mapper {
    fun mapToDomain(payload: InstitutionPayload): ExternalAccount {
        return object : ExternalAccount {
            override val institutionId = payload.rawId
            override val iban = "TR${payload.rawId.hashCode().coerceAtLeast(0)}..."
            override val balance = FinanceBigDecimal.fromDouble(payload.amount)
        }
    }
}
