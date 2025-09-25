package com.anshtya.movieinfo.common.data.local.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class UserPreferencesDataStore(
    private val datastore: DataStore<Preferences>
) {
    companion object {
        val DARK_MODE = stringPreferencesKey("dark_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val INCLUDE_ADULT_RESULTS =  booleanPreferencesKey("include_adult_results")
        val HIDE_ONBOARDING = booleanPreferencesKey("hide_onboarding")
    }

    val userData = datastore.data
        .map { preferences ->
            UserData(
                useDynamicColor = preferences[DYNAMIC_COLOR] == true,
                includeAdultResults = preferences[INCLUDE_ADULT_RESULTS] == true,
                darkMode = preferences[DARK_MODE]?.let { enumValueOf<SelectedDarkMode>(it) }
                    ?: SelectedDarkMode.SYSTEM,
                hideOnboarding = preferences[HIDE_ONBOARDING] == true
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        datastore.edit {
            it[DYNAMIC_COLOR] = useDynamicColor
        }
    }

    suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        datastore.edit {
            it[INCLUDE_ADULT_RESULTS] = includeAdultResults
        }
    }

    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        datastore.edit {
            it[DARK_MODE] = selectedDarkMode.name
        }
    }

    suspend fun setHideOnboarding(hideOnboarding: Boolean) {
        datastore.edit {
            it[HIDE_ONBOARDING] = hideOnboarding
        }
    }
}