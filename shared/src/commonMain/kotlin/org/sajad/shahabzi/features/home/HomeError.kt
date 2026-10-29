package org.sajad.shahabzi.features.home

sealed interface HomeError {
    data object LoadFailed : HomeError
    data object NoWallet : HomeError
}