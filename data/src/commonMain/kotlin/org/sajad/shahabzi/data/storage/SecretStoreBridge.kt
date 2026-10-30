package org.sajad.shahabzi.data.storage

/**
 * Thin native bridge for encrypted secret storage (iOS: Keychain, implemented in Swift).
 * Primitive types only. Methods return false/null on failure and must never log values.
 */
interface SecretStoreBridge {
    fun putSecret(key: String, secret: String): Boolean

    fun secretExists(key: String): Boolean

    /** Returns null if the secret is missing or could not be read. */
    fun getSecret(key: String): String?

    fun removeSecret(key: String): Boolean
}