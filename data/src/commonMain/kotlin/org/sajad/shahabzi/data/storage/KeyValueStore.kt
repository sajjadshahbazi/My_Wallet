package org.sajad.shahabzi.data.storage

/** Plain storage for NON-secret values only. Implementations throw [StorageException] on failure. */
internal interface KeyValueStore {
    fun getString(key: String): String?

    fun putString(key: String, value: String)

    fun remove(key: String)
}