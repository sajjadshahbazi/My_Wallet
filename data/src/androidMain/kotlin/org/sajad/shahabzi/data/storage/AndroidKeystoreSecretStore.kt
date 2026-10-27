package org.sajad.shahabzi.data.storage

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.ProviderException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import org.sajad.shahabzi.data.common.DataConstants

/**
 * Encrypts secrets with an AES-256-GCM key that lives in the Android Keystore (StrongBox when
 * available). Stored value = Base64(IV + ciphertext). The key is not yet bound to user
 * authentication (planned with the app lock).
 */
internal class AndroidKeystoreSecretStore(context: Context) : SecretStore {

    private val preferences =
        context.getSharedPreferences(DataConstants.SECRET_PREFS_FILE_NAME, Context.MODE_PRIVATE)

    override fun put(key: String, secret: String) {
        val payload = guarded("Could not encrypt a secret") {
            val cipher = Cipher.getInstance(DataConstants.AES_GCM_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
            cipher.iv + cipher.doFinal(secret.encodeToByteArray())
        }
        val saved = preferences.edit()
            .putString(key, Base64.encodeToString(payload, Base64.NO_WRAP))
            .commit()
        if (!saved) throw StorageException("Could not write an encrypted secret")
    }

    override fun get(key: String): String? {
        val encoded = preferences.getString(key, null) ?: return null
        return guarded("Could not decrypt a secret") {
            val payload = Base64.decode(encoded, Base64.NO_WRAP)
            val iv = payload.copyOfRange(0, DataConstants.GCM_IV_LENGTH_BYTES)
            val encrypted = payload.copyOfRange(DataConstants.GCM_IV_LENGTH_BYTES, payload.size)
            val cipher = Cipher.getInstance(DataConstants.AES_GCM_TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(DataConstants.GCM_TAG_LENGTH_BITS, iv),
            )
            cipher.doFinal(encrypted).decodeToString()
        }
    }

    override fun remove(key: String) {
        if (!preferences.edit().remove(key).commit()) {
            throw StorageException("Could not remove a secret")
        }
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(DataConstants.ANDROID_KEYSTORE).apply { load(null) }
        val existing = keyStore.getKey(DataConstants.SECRET_KEY_ALIAS, null) as? SecretKey
        return existing ?: createKey()
    }

    private fun createKey(): SecretKey {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                return generateKey(strongBox = true)
            } catch (e: ProviderException) {
                // StrongBox is not available on this device: fall back to the TEE-backed key.
            }
        }
        return generateKey(strongBox = false)
    }

    private fun generateKey(strongBox: Boolean): SecretKey {
        val builder = KeyGenParameterSpec.Builder(
            DataConstants.SECRET_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(DataConstants.AES_KEY_SIZE_BITS)
        if (strongBox && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            builder.setIsStrongBoxBacked(true)
        }
        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            DataConstants.ANDROID_KEYSTORE,
        )
        generator.init(builder.build())
        return generator.generateKey()
    }

    private inline fun <T> guarded(message: String, block: () -> T): T =
        try {
            block()
        } catch (e: GeneralSecurityException) {
            throw StorageException(message, e)
        } catch (e: ProviderException) {
            throw StorageException(message, e)
        } catch (e: IllegalArgumentException) {
            throw StorageException(message, e)
        }
}