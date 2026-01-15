package com.masenjoandroid.asociacionmayoresvillanueva.data.mapper

import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.User
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.UserProfile

fun User.toUserProfile(): UserProfile = UserProfile(
  id = id,
  nombre = nombre ?: "",
  email = "",
  password = "",
  rol = rol?.firstOrNull() ?: "usuario",
  xp = experiencia ?: 0,
  restricciones = emptyList(),
  actividades = emptyList(),
  quejas = emptyList(),
  validado = true
)
