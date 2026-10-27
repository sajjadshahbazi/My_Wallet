package org.sajad.shahabzi.data.storage

internal class FakeKeyValueStore : KeyValueStore {
    val values = mutableMapOf<String, String>()
    var failOnPut = false

    override fun getString(key: String): String? = values[key]

    override fun putString(key: String, value: String) {
        if (failOnPut) throw StorageException("fake failure")
        values[key] = value
    }

    override fun remove(key: String) {
        values.remove(key)
    }
}