package com.masenjoandroid.asociacionmayoresvillanueva.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completion_requests")
data class CompletionRequestEntity(
  @PrimaryKey val id: String,
  val userId: String,
  val activityId: String,
  val rating: Int,
  val difficultyText: String,
  val mediaUri: String?,
  val status: String, // "PENDING"
  val createdAt: Long
)
