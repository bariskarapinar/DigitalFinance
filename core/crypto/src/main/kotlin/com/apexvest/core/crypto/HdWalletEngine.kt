package com.apexvest.core.crypto

import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.DeterministicHierarchy
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.crypto.HDKeyDerivation
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException
import org.bitcoinj.wallet.DeterministicSeed
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ApexVest HD Wallet Engine.
 * Implements BIP-32/44 for private key derivation.
 */
@Singleton
class HdWalletEngine @Inject constructor() {

    private val secureRandom = SecureRandom()

    /**
     * Generates a new 12-word mnemonic seed phrase.
     */
    fun generateMnemonic(): List<String> {
        val entropy = ByteArray(16) // 128 bits for 12 words
        secureRandom.nextBytes(entropy)
        return MnemonicCode.INSTANCE.toMnemonic(entropy)
    }

    /**
     * Derives a private key for a specific path (BIP-44).
     * m / 44' / 60' / 0' / 0 / index (for Ethereum/EVM)
     */
    fun derivePrivateKey(mnemonic: List<String>, index: Int): String {
        val seed = DeterministicSeed(mnemonic, null, "", System.currentTimeMillis())
        val masterKey = HDKeyDerivation.createMasterPrivateKey(seed.seedBytes)
        val hierarchy = DeterministicHierarchy(masterKey)

        // Path: 44'/60'/0'/0/index
        val path = listOf(
            ChildNumber(44, true),
            ChildNumber(60, true),
            ChildNumber(0, true),
            ChildNumber.ZERO,
            ChildNumber(index)
        )

        val derivedKey = hierarchy.get(path, true, true)
        return derivedKey.privateKeyAsHex
    }
}
