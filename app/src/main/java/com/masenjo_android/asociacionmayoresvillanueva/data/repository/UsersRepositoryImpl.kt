package com.masenjo_android.asociacionmayoresvillanueva.data.repository

import com.masenjo_android.asociacionmayoresvillanueva.common.AppResult
import com.masenjo_android.asociacionmayoresvillanueva.data.firebase.FirebaseUsersDataSource
import com.masenjo_android.asociacionmayoresvillanueva.domain.model.UserProfile
import com.masenjo_android.asociacionmayoresvillanueva.domain.repository.UsersRepository

class UsersRepositoryImpl(
    private val dataSource: FirebaseUsersDataSource,
) : UsersRepository {

    override suspend fun getUserProfile(userId: String): AppResult<UserProfile> {
        return try {
            val doc = dataSource.fetchUser(userId)
            if (doc == null) {
                AppResult.Error("Usuario no encontrado")
            } else {
                // Placeholder de mapeo
                AppResult.Success(
                    UserProfile(
                        id = userId,
                        displayName = (doc["displayName"] as? String) ?: "Usuario",
                        restrictions = (doc["restrictions"] as? List<String>) ?: emptyList(),
                        xp = (doc["xp"] as? Number)?.toInt() ?: 0,
                    ),
                )
            }
        } catch (t: Throwable) {
            AppResult.Error("Error obteniendo perfil", t)
        }
    }
}
