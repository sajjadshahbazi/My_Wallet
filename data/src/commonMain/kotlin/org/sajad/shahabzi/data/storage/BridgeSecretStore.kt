package org.sajad.shahabzi.data.storage

internal class BridgeSecretStore(
    private val bridge: SecretStoreBridge,
) : SecretStore {

    override fun put(key: String, secret: String) {
        if (!bridge.putSecret(key, secret)) {
            throw StorageException("Could not store a secret")
        }
    }

    override fun get(key: String): String? {
        if (!bridge.secretExists(key)) return null
        return bridge.getSecret(key) ?: throw StorageException("Could not read a secret")
    }

    override fun remove(key: String) {
        if (!bridge.removeSecret(key)) {
            throw StorageException("Could not remove a secret")
        }
    }
}