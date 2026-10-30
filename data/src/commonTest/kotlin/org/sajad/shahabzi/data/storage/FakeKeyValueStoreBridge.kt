package org.sajad.shahabzi.data.storage

internal class FakeKeyValueStoreBridge : KeyValueStoreBridge {
    val values = mutableMapOf<String, String>()
    var failOnPut = false
    var failOnRemove = false

    override fun getValue(key: String): String? = values[key]

    override fun putValue(key: String, value: String): Boolean {
        if (failOnPut) return false
        values[key] = value
        return true
    }

    override fun removeValue(key: String): Boolean {
        if (failOnRemove) return false
        values.remove(key)
        return true
    }
}