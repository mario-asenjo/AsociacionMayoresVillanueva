package com.masenjoandroid.asociacionmayoresvillanueva.domain.model

data class ActivityItem(
  val id: String,
  val title: String,
  val dateTime: String,
  val placeName: String,
  val placePhotoUrl: String = "",
  val tags: List<String> = emptyList()
)
