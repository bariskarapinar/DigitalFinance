package com.apexvest.core.common

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SecurityUtils {

    private const val KEY_ALIAS = "FinanceAppKey"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    init {
        generateKeyIfNotExist()
    }

    private fun generateKeyIfNotExist() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray())
        
        // Combine IV and encrypted data: [IV_LENGTH (1 byte)][IV][ENCRYPTED_DATA]
        val result = ByteArray(1 + iv.size + encryptedData.size)
        result[0] = iv.size.toByte()
        System.arraycopy(iv, 0, result, 1, iv.size)
        System.arraycopy(encryptedData, 0, result, 1 + iv.size, encryptedData.size)
        
        return Base64.encodeToString(result, Base64.DEFAULT)
    }

    fun decrypt(encryptedBase64: String): String {
        val combined = Base64.decode(encryptedBase64, Base64.DEFAULT)
        val ivLength = combined[0].toInt()
        val iv = ByteArray(ivLength)
        System.arraycopy(combined, 1, iv, 0, ivLength)
        
        val encryptedDataLength = combined.size - 1 - ivLength
        val encryptedData = ByteArray(encryptedDataLength)
        System.arraycopy(combined, 1 + ivLength, encryptedData, 0, encryptedDataLength)
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        
        return String(cipher.doFinal(encryptedData))
    }
}
