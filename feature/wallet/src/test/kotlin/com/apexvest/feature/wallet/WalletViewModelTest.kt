package com.apexvest.feature.wallet

import com.apexvest.core.database.dao.WalletDao
import com.apexvest.core.database.entity.WalletEntity
import com.apexvest.core.network.ComplianceEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class WalletViewModelTest {

    private lateinit var viewModel: WalletViewModel
    private val walletDao: WalletDao = mock(WalletDao::class.java)
    private val complianceEngine: ComplianceEngine = mock(ComplianceEngine::class.java)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        `when`(walletDao.getWalletsForUser(anyString())).thenReturn(flowOf(emptyList()))
        viewModel = WalletViewModel(walletDao, complianceEngine)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `UpdateRiskProfile intent updates state`() {
        viewModel.onIntent(WalletIntent.UpdateRiskProfile(75))
        assertEquals(75, viewModel.uiState.value.riskProfileScore)
    }

    @Test
    fun `TriggerRebalance intent runs AI rebalancing simulation`() = runTest {
        viewModel.onIntent(WalletIntent.TriggerRebalance)
        // Note: Due to delay(1000) in runRebalance, we might need advanceTimeBy if using StandardTestDispatcher
        // But with UnconfinedTestDispatcher it might just work if not for the delay.
        // Let's test the state after the delay.
        advanceTimeBy(1100)
        assertFalse(viewModel.uiState.value.isRebalancing)
        assertEquals(0.0, viewModel.uiState.value.portfolioDrift, 0.0)
    }

    @Test
    fun `GenerateHDWallet intent updates web3Address`() {
        viewModel.onIntent(WalletIntent.GenerateHDWallet)
        assertNotNull(viewModel.uiState.value.web3Address)
        assertTrue(viewModel.uiState.value.web3Address!!.startsWith("0x"))
    }

    @Test
    fun `RefreshGasFees intent updates gas price`() {
        viewModel.onIntent(WalletIntent.RefreshGasFees)
        assertEquals(25.4, viewModel.uiState.value.gasPriceGwei, 0.0)
    }

    @Test
    fun `CreateEscrow intent locks escrow`() {
        viewModel.onIntent(WalletIntent.CreateEscrow(100.0, "beneficiary", 7))
        assertTrue(viewModel.uiState.value.isEscrowLocked)
    }
}
