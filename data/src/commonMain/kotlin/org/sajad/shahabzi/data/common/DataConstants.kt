package org.sajad.shahabzi.data.common

/** All fixed values of the data module live here. Reference them, never repeat the literal. */
object DataConstants {
    const val WALLET_CORE_LIBRARY_NAME = "TrustWalletCore"

    const val COIN_ETHEREUM = "ETHEREUM"

    const val MNEMONIC_STRENGTH_BITS_12_WORDS = 128
    const val MNEMONIC_STRENGTH_BITS_24_WORDS = 256

    // Storage layout v1 (see docs/wallet-lifecycle.md). Changing these needs a migration.
    const val STORAGE_VERSION = 1
    const val KEY_STORAGE_VERSION = "storage_version"
    const val KEY_WALLET_IDS = "wallet_ids"
    const val KEY_PREFIX_WALLET_ADDRESS = "wallet_address."
    const val KEY_PREFIX_WALLET_MNEMONIC = "wallet_mnemonic."
    const val KEY_SEPARATOR = "."
    const val ID_SEPARATOR = ","

    // Android storage
    const val PREFS_FILE_NAME = "mywallet_prefs"
    const val SECRET_PREFS_FILE_NAME = "mywallet_secrets"
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val SECRET_KEY_ALIAS = "mywallet_secret_key"
    const val AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding"
    const val AES_KEY_SIZE_BITS = 256
    const val GCM_IV_LENGTH_BYTES = 12
    const val GCM_TAG_LENGTH_BITS = 128
}