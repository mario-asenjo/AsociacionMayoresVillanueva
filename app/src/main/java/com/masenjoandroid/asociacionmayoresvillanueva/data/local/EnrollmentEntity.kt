package com.masenjoandroid.asociacionmayoresvillanueva.data.local

import androidx.room.Entity

@Entity(tableName = "enrollments", primaryKeys = ["userId", "activityId"])
data class EnrollmentEntity(val userId: String, val activityId: String, val enrolledAt: Long)
