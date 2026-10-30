package org.sajad.shahabzi.data.storage

internal class FakeSecretStoreBridge : SecretStoreBridge {
    val values = mutableMapOf<String, String>()
    var failOnPut = false
    var failOnRead = false
    var failOnRemove = false

    override fun putSecret(key: String, secret: String): Boolean {
        if (failOnPut) return false
        values[key] = secret
        return true
    }

    override fun secretExists(key: String): Boolean = key in values

    override fun getSecret(key: String): String? = if (failOnRead) null else values[key]

    override fun removeSecret(key: String): Boolean {
        if (failOnRemove) return false
        values.remove(key)
        return true
    }
}