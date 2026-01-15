package com.masenjoandroid.asociacionmayoresvillanueva.data.repository

import com.masenjoandroid.asociacionmayoresvillanueva.data.local.EnrollmentDao
import com.masenjoandroid.asociacionmayoresvillanueva.data.local.EnrollmentEntity
import com.masenjoandroid.asociacionmayoresvillanueva.domain.repository.EnrollmentRepository

class EnrollmentRepositoryImpl(private val dao: EnrollmentDao) : EnrollmentRepository {

  override suspend fun enroll(userId: String, activityId: String) {
    dao.upsert(
      EnrollmentEntity(
        userId = userId,
        activityId = activityId,
        enrolledAt = System.currentTimeMillis()
      )
    )
  }

  override suspend fun unenroll(userId: String, activityId: String) {
    dao.delete(userId, activityId)
  }

  override suspend fun getEnrolledActivityIds(userId: String): List<String> =
    dao.getActivityIds(userId)
}
