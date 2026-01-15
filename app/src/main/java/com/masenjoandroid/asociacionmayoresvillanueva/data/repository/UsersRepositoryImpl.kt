package com.masenjoandroid.asociacionmayoresvillanueva.data.repository

import android.content.Context
import com.masenjoandroid.asociacionmayoresvillanueva.data.firebase.FirebaseUsersDataSource
import com.masenjoandroid.asociacionmayoresvillanueva.data.mapper.toUserProfile
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.UserProfile
import java.util.UUID

class UsersRepositoryImpl(context: Context) {

  private val dataSource = FirebaseUsersDataSource(context)

  // Usuarios registrados en tiempo de ejecución (simulación)
  private val registeredUsers = mutableListOf<UserProfile>()

  // =====================
  // DATOS ORIGINALES (JSON)
  // =====================

  suspend fun getUsers(): List<UserProfile> = dataSource.fetchUsers().map { it.toUserProfile() }

  suspend fun getUserById(userId: String): UserProfile? = getUsers().find { it.id == userId }

  // =====================
  // REGISTRO
  // =====================

  suspend fun registerUser(
    email: String,
    password: String,
    rol: String,
    restricciones: List<String>
  ): Result<Unit> {
    if (registeredUsers.any { it.email == email }) {
      return Result.failure(Exception("Correo ya registrado"))
    }

    val user = UserProfile(
      id = UUID.randomUUID().toString(),
      nombre = "",
      email = email,
      password = password,
      rol = rol,
      xp = 0,
      restricciones = restricciones,
      actividades = emptyList(),
      quejas = emptyList(),
      validado = rol != "monitor"
    )

    registeredUsers.add(user)
    return Result.success(Unit)
  }

  // =====================
  // LOGIN
  // =====================

  suspend fun login(email: String, password: String): Result<UserProfile> {
    val user = registeredUsers.find { it.email == email }
      ?: return Result.failure(Exception("Usuario no registrado"))

    if (user.password != password) {
      return Result.failure(Exception("Contraseña incorrecta"))
    }

    if (user.rol == "monitor" && !user.validado) {
      return Result.failure(Exception("Monitor pendiente de validación"))
    }

    return Result.success(user)
  }

  // =====================
  // VALIDACIÓN DE MONITOR
  // =====================

  suspend fun validateMonitor(userId: String) {
    val index = registeredUsers.indexOfFirst { it.id == userId }
    if (index != -1) {
      registeredUsers[index] =
        registeredUsers[index].copy(validado = true)
    }
  }
}
