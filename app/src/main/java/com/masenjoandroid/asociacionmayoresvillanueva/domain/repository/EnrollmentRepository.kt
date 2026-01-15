package com.masenjoandroid.asociacionmayoresvillanueva.domain.repository

interface EnrollmentRepository {
  suspend fun enroll(userId: String, activityId: String)
  suspend fun unenroll(userId: String, activityId: String)
  suspend fun getEnrolledActivityIds(userId: String): List<String>
}
