package com.dicoding.picodiploma.loginwithanimation.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }

    suspend fun saveLogin(token: String) {
        dataStore.edit { pref ->
            pref[TOKEN_KEY] = token
        }
    }

    suspend fun saveSession(user: UserModel?) {
        user?.let {
            dataStore.edit { preferences ->
                preferences[EMAIL_KEY] = it.email
                preferences[TOKEN_KEY] = it.token
                preferences[IS_LOGIN_KEY] = true
            }
            Log.d("UserPreference", "User session saved: $user")
        } ?: run {
            Log.e("UserPreference", "User model is null. Cannot save session.")
        }
    }
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}