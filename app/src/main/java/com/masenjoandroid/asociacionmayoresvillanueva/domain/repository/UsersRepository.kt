package com.masenjoandroid.asociacionmayoresvillanueva.domain.repository

import com.masenjoandroid.asociacionmayoresvillanueva.common.AppResult
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.UserProfile

interface UsersRepository {
  suspend fun getUserProfile(userId: String): AppResult<UserProfile>
}
