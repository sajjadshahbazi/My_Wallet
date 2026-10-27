package org.sajad.shahabzi.data.storage

internal class FakeSecretStore : SecretStore {
    val values = mutableMapOf<String, String>()
    var failOnPut = false

    override fun put(key: String, secret: String) {
        if (failOnPut) throw StorageException("fake failure")
        values[key] = secret
    }

    override fun get(key: String): String? = values[key]

    override fun remove(key: String) {
        values.remove(key)
    }
}