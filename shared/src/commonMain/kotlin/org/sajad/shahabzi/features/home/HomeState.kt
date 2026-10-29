package org.sajad.shahabzi.features.home

data class HomeState(
    val isLoading: Boolean = true,
    val address: String? = null,
    val error: HomeError? = null,
)