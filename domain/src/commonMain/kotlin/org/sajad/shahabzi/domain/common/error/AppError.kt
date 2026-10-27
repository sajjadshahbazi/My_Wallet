package org.sajad.shahabzi.domain.common.error

sealed interface AppError {
    data object InvalidMnemonic : AppError
    data object MnemonicGenerationFailed : AppError
    data object DerivationFailed : AppError
    data object WalletAlreadyExists : AppError
    data object StorageFailed : AppError
}