package com.masenjoandroid.asociacionmayoresvillanueva.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
  val id: String,
  val nombre: String,
  val email: String,
  val password: String,
  val rol: String,
  val xp: Int,
  val restricciones: List<String>,
  val actividades: List<String>,
  val quejas: List<String>,
  val validado: Boolean = true
)
