package com.masenjoandroid.asociacionmayoresvillanueva.data.repository

import android.content.Context
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.UserProfile
import java.util.UUID
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UsersRepositoryImpl(context: Context) {

  private val appContext = context.applicationContext

  private val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

  private val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
  }

  // ---- API ----

  suspend fun registerUser(
    email: String,
    password: String,
    rol: String,
    restricciones: List<String>
  ): Result<Unit> {
    val users = loadUsers().toMutableList()

    if (users.any { it.email.equals(email, ignoreCase = true) }) {
      return Result.failure(Exception("Correo ya registrado"))
    }

    val user = UserProfile(
      id = UUID.randomUUID().toString(),
      nombre = "",
      email = email.trim(),
      password = password,
      rol = rol,
      xp = 0,
      restricciones = restricciones,
      actividades = emptyList(),
      quejas = emptyList(),
      validado = rol != "monitor"
    )

    users.add(user)
    saveUsers(users)
    return Result.success(Unit)
  }

  suspend fun login(email: String, password: String): Result<UserProfile> {
    val users = loadUsers()
    val user = users.find { it.email.equals(email.trim(), ignoreCase = true) }
      ?: return Result.failure(Exception("Usuario no registrado"))

    if (user.password != password) {
      return Result.failure(Exception("Contraseña incorrecta"))
    }

    if (user.rol == "monitor" && !user.validado) {
      return Result.failure(Exception("Monitor pendiente de validación"))
    }

    // Guardamos sesión
    saveSession(user)
    return Result.success(user)
  }

  fun getSessionUserId(): String? = prefs.getString(KEY_SESSION_USER_ID, null)
  fun clearSession() {
    prefs.edit()
      .remove(KEY_SESSION_USER_ID)
      .remove(KEY_SESSION_ROLE)
      .apply()
  }

  // ---- storage ----

  private fun loadUsers(): List<UserProfile> {
    val raw = prefs.getString(KEY_USERS, null) ?: return emptyList()
    return runCatching { json.decodeFromString<List<UserProfile>>(raw) }
      .getOrElse { emptyList() }
  }

  private fun saveUsers(users: List<UserProfile>) {
    val raw = json.encodeToString(users)
    prefs.edit().putString(KEY_USERS, raw).apply()
  }

  private fun saveSession(user: UserProfile) {
    prefs.edit()
      .putString(KEY_SESSION_USER_ID, user.id)
      .putString(KEY_SESSION_ROLE, user.rol)
      .apply()
  }

  companion object {
    private const val PREFS = "users_repo_prefs"
    private const val KEY_USERS = "users"
    private const val KEY_SESSION_USER_ID = "session_user_id"
    private const val KEY_SESSION_ROLE = "session_role"
  }
}
