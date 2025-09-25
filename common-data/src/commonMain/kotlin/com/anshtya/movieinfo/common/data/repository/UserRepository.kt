package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.local.datastore.preferences.SelectedDarkMode
import com.anshtya.movieinfo.common.data.local.datastore.preferences.UserData
import com.anshtya.movieinfo.common.data.model.AccountDetails
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userData: Flow<UserData>

    suspend fun getAccountDetails(): AccountDetails?

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setAdultResultPreference(includeAdultResults: Boolean)

    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode)

    suspend fun updateAccountDetails(accountId: Int): Result<Unit>

    suspend fun setHideOnboarding(hideOnboarding: Boolean)
}