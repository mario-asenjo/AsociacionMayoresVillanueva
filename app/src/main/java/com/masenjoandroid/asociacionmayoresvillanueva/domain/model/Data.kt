package com.masenjoandroid.asociacionmayoresvillanueva.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
  val id: String,
  val nombre: String? = null,
  val fecha_hora: String? = null, // la convertiremos de "__time__"
  val etiquetas: List<String>? = null,
  val imagen: String? = null,
  val ubicacion: String? = null
)

@Serializable
data class User(
  val id: String,
  val nombre: String? = null,
  val experiencia: Int? = null,
  val rol: List<String>? = null,
  val email: String? = null,
  val password: String? = null,
  val restricciones: List<String>? = null,
  val quejas: List<String>? = null,
  val actividades: List<String>? = null

)
