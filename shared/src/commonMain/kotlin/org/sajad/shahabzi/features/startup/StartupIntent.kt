package org.sajad.shahabzi.features.startup

sealed interface StartupIntent {
    data object RetryClicked : StartupIntent
}