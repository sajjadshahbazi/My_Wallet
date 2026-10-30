package org.sajad.shahabzi.data.storage

/** Thin native bridge for NON-secret values (iOS: Keychain, implemented in Swift). */
interface KeyValueStoreBridge {
    fun getValue(key: String): String?

    fun putValue(key: String, value: String): Boolean

    fun removeValue(key: String): Boolean
}