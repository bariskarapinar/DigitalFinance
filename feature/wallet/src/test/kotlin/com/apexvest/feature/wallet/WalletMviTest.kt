package com.apexvest.feature.wallet

import org.junit.Assert.assertEquals
import org.junit.Test

class WalletMviTest {

    @Test
    fun `WalletState initial values are correct`() {
        val state = WalletState()
        assertEquals(50, state.riskProfileScore)
        assertEquals(100, state.trustScore)
        assertEquals(SecurityLevel.NORMAL, state.securityLevel)
        assertEquals(0.0, state.gasPriceGwei, 0.0)
    }

    @Test
    fun `WalletIntent sub-classes are accessible`() {
        val intent: WalletIntent = WalletIntent.TriggerRebalance
        assertEquals(WalletIntent.TriggerRebalance, intent)
    }
}
