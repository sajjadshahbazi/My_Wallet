package org.sajad.shahabzi.features.startup

sealed interface StartupError {
    data object StorageUnavailable : StartupError
}