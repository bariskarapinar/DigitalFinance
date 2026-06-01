package com.apexvest.feature.wallet

import com.apexvest.core.common.UiEffect
import com.apexvest.core.common.UiIntent
import com.apexvest.core.common.UiState
import com.apexvest.core.database.entity.WalletEntity

data class WalletState(
    val wallets: List<WalletEntity> = emptyList(),
    val totalBalanceUsd: Double = 0.0,
    val isEscrowLocked: Boolean = false,
    val isLoading: Boolean = false,
    
    // AI robo-advisory
    val riskProfileScore: Int = 50,
    val isRebalancing: Boolean = false,
    val portfolioDrift: Double = 0.0,
    
    // Web3 Gateway
    val web3Address: String? = null,
    val gasPriceGwei: Double = 0.0,
    val stakingYieldPercent: Double = 0.0,
    
    // Multi-Currency Settlement
    val fxRates: Map<String, Double> = emptyMap(),
    
    // Fraud Analytics
    val trustScore: Int = 100,
    val securityLevel: SecurityLevel = SecurityLevel.NORMAL
) : UiState

enum class SecurityLevel { NORMAL, ELEVATED, RESTRICTED }

sealed interface WalletIntent : UiIntent {
    data object LoadWallets : WalletIntent
    data class SwiftTransfer(val amount: Double, val targetIban: String) : WalletIntent
    data class AtomicSwap(val from: String, val to: String, val amount: Double) : WalletIntent
    
    // AI-Driven Robo-Advisory
    data class UpdateRiskProfile(val newScore: Int) : WalletIntent
    data object TriggerRebalance : WalletIntent
    
    // Web3 Gateway
    data object GenerateHDWallet : WalletIntent
    data class StakeTokens(val amount: Double, val network: String) : WalletIntent
    data object RefreshGasFees : WalletIntent
    
    // Multi-Currency Settlement
    data class InternationalTransfer(val amount: Double, val targetIban: String, val currency: String) : WalletIntent
    
    // Programmable Escrow
    data class CreateEscrow(val amount: Double, val beneficiary: String, val timelockDays: Int) : WalletIntent
    data object ReleaseEscrow : WalletIntent
    data object OpenArbitration : WalletIntent
    
    // Fraud Analytics & Behavioral Telemetry
    data class LogInteractionTelemetry(val metrics: String) : WalletIntent
    data object StepUpAuthentication : WalletIntent
}

sealed interface WalletEffect : UiEffect {
    data class ShowSecurityAlert(val message: String) : WalletEffect
    data object BiometricChallenge : WalletEffect
    data class NavigateToWeb3Details(val address: String) : WalletEffect
    data class FraudFlagAlert(val reason: String) : WalletEffect
}
