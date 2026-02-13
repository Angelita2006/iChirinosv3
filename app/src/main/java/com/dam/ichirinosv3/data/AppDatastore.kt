package com.dam.ichirinosv3.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dam.ichirinosv3.R

class AppDatastore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = context.resources.getString(R.string.value_pref_dsname)
    )

    fun getDataStore(): DataStore<Preferences> {
        return context.dataStore
    }
}
