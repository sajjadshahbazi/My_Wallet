package org.sajad.shahabzi.data.storage

import android.content.Context
import org.sajad.shahabzi.data.common.DataConstants

internal class AndroidKeyValueStore(context: Context) : KeyValueStore {

    private val preferences =
        context.getSharedPreferences(DataConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)

    override fun getString(key: String): String? = preferences.getString(key, null)

    override fun putString(key: String, value: String) {
        if (!preferences.edit().putString(key, value).commit()) {
            throw StorageException("Could not write a value")
        }
    }

    override fun remove(key: String) {
        if (!preferences.edit().remove(key).commit()) {
            throw StorageException("Could not remove a value")
        }
    }
}