package org.sajad.shahabzi.domain.common.result


import org.sajad.shahabzi.domain.common.error.AppError

sealed interface AppResult<out T> {
    data class Success<out T>(val value: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}