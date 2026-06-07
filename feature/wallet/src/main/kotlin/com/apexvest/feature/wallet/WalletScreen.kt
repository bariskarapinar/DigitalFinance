package com.apexvest.feature.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apexvest.core.designsystem.*
import com.apexvest.core.designsystem.components.NeonCard
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showBiometricPrompt by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ApexVault Pro", color = NeonCyan, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = DeepBlack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepBlack, NeonPurple.copy(alpha = 0.1f), DeepBlack)
                    )
                )
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NeonCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("CONSOLIDATED LIQUIDITY (USD)", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "$${String.format(Locale.US, "%,.2f", state.totalBalanceUsd)}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Multi-Currency Ledger
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                state.wallets.forEach { wallet ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = GlassWhite)
                    ) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(wallet.currencyCode, style = MaterialTheme.typography.labelSmall, color = NeonPink)
                            Text(String.format(Locale.US, "%.0f", wallet.balance), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI & Security Metrics
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("AI REBALANCING", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Text(if (state.isRebalancing) "ACTIVE" else "IDLE", color = Color.White)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("TRUST SCORE", style = MaterialTheme.typography.labelSmall, color = NeonPink)
                    Text("${state.trustScore}/100", color = Color.White)
                }
            }

            if (state.web3Address != null) {
                Spacer(modifier = Modifier.height(16.dp))
                NeonCard {
                    Column {
                        Text("WEB3 GATEWAY", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                        Text(state.web3Address!!, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onIntent(WalletIntent.ReleaseEscrow) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
            ) {
                Text("Release Escrow: Biometric Confirmation")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.onIntent(WalletIntent.TriggerRebalance) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Run AI MPT", color = NeonCyan)
                }
                OutlinedButton(
                    onClick = { viewModel.onIntent(WalletIntent.GenerateHDWallet) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Web3 Init", color = NeonCyan)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { viewModel.onIntent(WalletIntent.AtomicSwap("USD", "TRY", 1000.0)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Atomic FX Swap: 1,000 USD to TRY", color = NeonCyan)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "ISO 20022 Compliance: ACTIVE",
                style = MaterialTheme.typography.labelSmall,
                color = BullishGreen
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is WalletEffect.BiometricChallenge -> showBiometricPrompt = true
                is WalletEffect.ShowSecurityAlert -> { /* handle or log */ }
                is WalletEffect.NavigateToWeb3Details -> { /* handle navigation */ }
                is WalletEffect.FraudFlagAlert -> { /* show alert */ }
            }
        }
    }

    if (showBiometricPrompt) {
        AlertDialog(
            onDismissRequest = { showBiometricPrompt = false },
            containerColor = GlassBlack,
            confirmButton = {
                TextButton(onClick = { showBiometricPrompt = false }) {
                    Text("Verify & Release", color = NeonCyan)
                }
            },
            title = { Text("Biometric Intent Binding") },
            text = { Text("Securely sign the transaction payload via hardware-backed verification.") }
        )
    }
}
