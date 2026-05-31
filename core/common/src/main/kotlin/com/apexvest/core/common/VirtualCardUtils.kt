package com.apexvest.core.common

import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

/**
 * ApexVest Dynamic Virtual Card Engine.
 * Implements TOTP for Dynamic CVV rotation.
 */
object VirtualCardUtils {

    /**
     * Generates a 3-digit Dynamic CVV that rotates every 60 seconds.
     * Uses a HMAC-SHA1 based TOTP algorithm.
     */
    fun generateDynamicCVV(secret: String): String {
        val timeStep = 60000L // 60 seconds
        val counter = System.currentTimeMillis() / timeStep
        
        val key = secret.toByteArray()
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(key, "HmacSHA1"))
        
        val data = ByteArray(8)
        var tempCounter = counter
        for (i in 7 downTo 0) {
            data[i] = (tempCounter and 0xff).toByte()
            tempCounter = tempCounter shr 8
        }
        
        val hash = mac.doFinal(data)
        val offset = hash[hash.size - 1].toInt() and 0xf
        val truncatedHash = ((hash[offset].toInt() and 0x7f) shl 24) or
                           ((hash[offset + 1].toInt() and 0xff) shl 16) or
                           ((hash[offset + 2].toInt() and 0xff) shl 8) or
                           (hash[offset + 3].toInt() and 0xff)
        
        val cvv = truncatedHash % 10.0.pow(3).toInt()
        return String.format(Locale.US, "%03d", cvv)
    }

    /**
     * Simulates PAN tokenization.
     */
    fun generateTokenizedPAN(): String {
        val random = Random()
        return "4000 1234 5678 ${1000 + random.nextInt(9000)}"
    }
}
