package com.masenjoandroid.asociacionmayoresvillanueva.domain.model

data class UserProfile(
  val id: String,
  val displayName: String,
  val restrictions: List<String>,
  val xp: Int
)
