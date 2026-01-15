package com.masenjoandroid.asociacionmayoresvillanueva.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
  val id: String,
  val nombre: String? = null,
  val fecha_hora: String? = null,
  val etiquetas: List<String>? = null,
  val imagen: String? = null,
  val ubicacion: String? = null
)
