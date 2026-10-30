package org.sajad.shahabzi.data.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class BridgeKeyValueStoreTest {

    private val bridge = FakeKeyValueStoreBridge()
    private val store = BridgeKeyValueStore(bridge)

    @Test
    fun putString_thenGetString_returnsTheValue() {
        store.putString("key", "value")

        assertEquals("value", store.getString("key"))
    }

    @Test
    fun getString_whenMissing_returnsNull() {
        assertNull(store.getString("missing"))
    }

    @Test
    fun putString_whenBridgeFails_throwsStorageException() {
        bridge.failOnPut = true

        assertFailsWith<StorageException> { store.putString("key", "value") }
    }

    @Test
    fun remove_whenBridgeFails_throwsStorageException() {
        bridge.failOnRemove = true

        assertFailsWith<StorageException> { store.remove("key") }
    }
}