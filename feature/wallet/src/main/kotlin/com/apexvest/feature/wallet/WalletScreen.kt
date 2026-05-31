package com.apexvest.feature.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    var showBiometricPrompt by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("My Wallet") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Total Balance", style = MaterialTheme.typography.titleMedium)
                    val balance = user?.balance ?: 0.0
                    Text(
                        "$${String.format(Locale.US, "%,.2f", balance)}",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showBiometricPrompt = true },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Transfer Funds")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { /* Simulated Virtual Card Generation */ },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("Generate Virtual Card")
            }
        }
    }

    if (showBiometricPrompt) {
        AlertDialog(
            onDismissRequest = { showBiometricPrompt = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.transfer(100.0)
                    showBiometricPrompt = false
                }) {
                    Text("Authenticate")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBiometricPrompt = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Biometric Authentication") },
            text = { Text("Please authenticate to authorize the transfer of $100.00") }
        )
    }
}
