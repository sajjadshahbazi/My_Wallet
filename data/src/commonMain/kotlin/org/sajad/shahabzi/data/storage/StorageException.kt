package org.sajad.shahabzi.data.storage

/** Thrown by storage implementations. The message must never contain secret values. */
internal class StorageException(message: String, cause: Throwable? = null) : Exception(message, cause)