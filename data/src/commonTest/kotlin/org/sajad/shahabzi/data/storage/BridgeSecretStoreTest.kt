package org.sajad.shahabzi.data.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class BridgeSecretStoreTest {

    private val bridge = FakeSecretStoreBridge()
    private val store = BridgeSecretStore(bridge)

    @Test
    fun put_thenGet_returnsTheSecret() {
        store.put("key", "secret")

        assertEquals("secret", store.get("key"))
    }

    @Test
    fun put_whenBridgeFails_throwsStorageException() {
        bridge.failOnPut = true

        assertFailsWith<StorageException> { store.put("key", "secret") }
    }

    @Test
    fun get_whenMissing_returnsNull() {
        assertNull(store.get("missing"))
    }

    @Test
    fun get_whenSecretExistsButCannotBeRead_throwsStorageException() {
        store.put("key", "secret")
        bridge.failOnRead = true

        assertFailsWith<StorageException> { store.get("key") }
    }

    @Test
    fun remove_deletesTheSecret() {
        store.put("key", "secret")

        store.remove("key")

        assertNull(store.get("key"))
    }

    @Test
    fun remove_whenBridgeFails_throwsStorageException() {
        bridge.failOnRemove = true

        assertFailsWith<StorageException> { store.remove("key") }
    }
}