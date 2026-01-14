package com.masenjoandroid.asociacionmayoresvillanueva.domain.model

data class CompletionRequest(
  val id: String,
  val activityId: String,
  val userId: String,
  val durationMinutes: Int,
  val status: String
)
