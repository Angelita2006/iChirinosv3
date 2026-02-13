package com.dam.ichirinosv3.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.data.Preferencias
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class MainRepository(private val context: Context, private val dataStore: DataStore<Preferences>) {

    private companion object {
        val DS_NAME = stringPreferencesKey("ds_name")
        val DB_NAME = stringPreferencesKey("db_name")
        val SHOW_LOGIN_ONSTART = booleanPreferencesKey("show_login_onstart")
        val DEFAULT_TIME_SPLASH = intPreferencesKey("default_time_splash")
//        const val TAG = "MainRepository"
    }

    fun getPreferences(): Flow<Preferencias> =
        dataStore.data
            .catch {
                if (it is IOException) {
//                Log.e(TAG, "Error reading preferences!!", it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
                Preferencias(
                    preferences[DS_NAME] ?: context.getString(R.string.value_pref_dsname),
                    preferences[DB_NAME] ?: context.getString(R.string.value_pref_dbname),
                    preferences[SHOW_LOGIN_ONSTART]
                        ?: context.getString(R.string.value_pref_showloginonstart).toBoolean(),
                    preferences[DEFAULT_TIME_SPLASH]
                        ?: context.getString(R.string.value_pref_defaulttimesplash).toInt()
                )
            }

    suspend fun savePreferences(prefs: Preferencias) {
        dataStore.edit { preferences ->
            preferences[DS_NAME] = prefs.dsName
            preferences[DB_NAME] = prefs.dbName
            preferences[SHOW_LOGIN_ONSTART] = prefs.showLoginOnStart
            preferences[DEFAULT_TIME_SPLASH] = prefs.defaultTimeSplash
        }
    }
}
