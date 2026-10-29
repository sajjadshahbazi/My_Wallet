package org.sajad.shahabzi.features.home

sealed interface HomeIntent {
    data object RetryClicked : HomeIntent
    data object AddWalletClicked : HomeIntent
}