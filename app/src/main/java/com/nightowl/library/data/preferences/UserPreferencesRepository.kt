package com.nightowl.library.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nightowl.library.data.model.ReservationSort
import com.nightowl.library.data.model.ThemeMode
import com.nightowl.library.data.model.UserPreferences
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val RESERVATION_SORT = stringPreferencesKey("reservation_sort")
    }

    val preferences: Flow<UserPreferences> = context.dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { prefs ->
            UserPreferences(
                themeMode = ThemeMode.entries.firstOrNull { it.name == prefs[Keys.THEME_MODE] }
                    ?: ThemeMode.SYSTEM,
                reservationSort = ReservationSort.entries.firstOrNull { it.name == prefs[Keys.RESERVATION_SORT] }
                    ?: ReservationSort.BY_TIME
            )
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setReservationSort(sort: ReservationSort) {
        context.dataStore.edit { it[Keys.RESERVATION_SORT] = sort.name }
    }
}