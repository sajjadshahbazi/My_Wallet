package org.sajad.shahabzi.data.storage

internal class BridgeKeyValueStore(
    private val bridge: KeyValueStoreBridge,
) : KeyValueStore {

    override fun getString(key: String): String? = bridge.getValue(key)

    override fun putString(key: String, value: String) {
        if (!bridge.putValue(key, value)) {
            throw StorageException("Could not write a value")
        }
    }

    override fun remove(key: String) {
        if (!bridge.removeValue(key)) {
            throw StorageException("Could not remove a value")
        }
    }
}