package com.masenjoandroid.asociacionmayoresvillanueva.domain.model

data class FeedbackItem(
  val id: String,
  val userName: String,
  val userPhotoUrl: String = "",
  val activityTitle: String,

  // 1. ¿Cómo te has sentido? (1-5)
  val feelingRating: Int,

  // 2. ¿Qué te ha costado? (Mensaje libre)
  val difficultyMessage: String,

  // 3. Recurso multimedia (URI de la foto o video adjunto)
  // Si es null o vacío, es que no adjuntó nada.
  val attachmentUri: String? = null,
  val isVideo: Boolean = false, // Para saber si pintar un icono de "Play" encima

  val isReviewed: Boolean = false
)
