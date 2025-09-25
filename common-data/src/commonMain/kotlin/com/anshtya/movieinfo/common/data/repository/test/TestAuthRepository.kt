package com.anshtya.movieinfo.common.data.repository.test

import com.anshtya.movieinfo.common.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestAuthRepository : AuthRepository {
    private var generateError = false

    private val _isLoggedIn = MutableStateFlow(false)
    override val isLoggedIn = _isLoggedIn.asStateFlow()

    override suspend fun login(username: String, password: String): Result<Unit> {
        return if (generateError) {
            Result.failure(Exception("error"))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun logout(accountId: Int): Result<Unit> {
        return if (generateError) {
            Result.failure(Exception("error"))
        } else {
            Result.success(Unit)
        }
    }

    fun setAuthStatus(isLoggedIn: Boolean) {
        _isLoggedIn.update { isLoggedIn }
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}