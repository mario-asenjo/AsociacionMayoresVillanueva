package com.masenjo_android.asociacionmayoresvillanueva.domain.repository

import com.masenjo_android.asociacionmayoresvillanueva.common.AppResult
import com.masenjo_android.asociacionmayoresvillanueva.domain.model.UserProfile

interface UsersRepository {
    suspend fun getUserProfile(userId: String): AppResult<UserProfile>
}
