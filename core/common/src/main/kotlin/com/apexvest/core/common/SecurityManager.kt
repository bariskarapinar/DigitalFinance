package com.apexvest.core.common

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * ApexVest Enterprise Security Manager.
 * Manages hardware-backed keys in Android Keystore.
 */
object SecurityManager {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val DB_KEY_ALIAS = "ApexVestDbKey"
    private const val AES_TRANSFORMATION = "AES/GCM/NoPadding"

    init {
        generateDbKeyIfNotExist()
    }

    private fun generateDbKeyIfNotExist() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(DB_KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, 
                ANDROID_KEYSTORE
            )
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    DB_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(false) // For static DB passphrases if needed, or manage IVs
                    .build()
            )
            keyGenerator.generateKey()
        }
    }

    /**
     * In a production enterprise app, we'd generate a random 256-bit passphrase,
     * encrypt it with a Keystore key, and store the encrypted version in SharedPreferences.
     * For this demo, we simulate a secure hardware-backed retrieval.
     */
    fun getDatabasePassphrase(): ByteArray {
        // Implementation would normally retrieve and decrypt a stored passphrase
        // Here we return a deterministic but secured "mock" passphrase for the demo
        return "apex-vest-production-ultra-secure-passphrase-2025".toByteArray()
    }
}
