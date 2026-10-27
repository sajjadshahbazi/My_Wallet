package org.sajad.shahabzi.data.storage

/** Encrypted storage for secrets. Implementations throw [StorageException] on failure. */
internal interface SecretStore {
    fun put(key: String, secret: String)

    /** Returns null only if there is no secret for [key]. */
    fun get(key: String): String?

    fun remove(key: String)
}