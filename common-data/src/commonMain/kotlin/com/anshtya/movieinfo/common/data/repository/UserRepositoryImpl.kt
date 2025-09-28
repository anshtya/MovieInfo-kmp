package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.common.data.local.database.entity.asModel
import com.anshtya.movieinfo.common.data.local.datastore.preferences.SelectedDarkMode
import com.anshtya.movieinfo.common.data.local.datastore.preferences.UserData
import com.anshtya.movieinfo.common.data.local.datastore.preferences.UserPreferencesDataStore
import com.anshtya.movieinfo.common.data.model.AccountDetails
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.util.asEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
internal class UserRepositoryImpl(
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val tmdbClient: TmdbClient,
    private val accountDetailsDao: AccountDetailsDao,
) : UserRepository {
    override val userData: Flow<UserData> = userPreferencesDataStore.userData

    override suspend fun getAccountDetails(): AccountDetails? = accountDetailsDao
        .getAccountDetails()?.asModel()

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferencesDataStore.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        userPreferencesDataStore.setAdultResultPreference(includeAdultResults)
    }

    override suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        userPreferencesDataStore.setDarkModePreference(selectedDarkMode)
    }

    override suspend fun updateAccountDetails(accountId: Int): Result<Unit> {
        return runCatching {
            val accountDetails = tmdbClient.getAccountDetailsWithId(accountId)
                .getOrThrow().asEntity()
            accountDetailsDao.addAccountDetails(accountDetails)
            userPreferencesDataStore.setAdultResultPreference(accountDetails.includeAdult)
        }
    }

    override suspend fun setHideOnboarding(hideOnboarding: Boolean) {
        userPreferencesDataStore.setHideOnboarding(hideOnboarding)
    }
}