package com.masenjoandroid.asociacionmayoresvillanueva.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EnrollmentDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsert(entity: EnrollmentEntity)

  @Query("DELETE FROM enrollments WHERE userId = :userId AND activityId = :activityId")
  suspend fun delete(userId: String, activityId: String)

  @Query("SELECT activityId FROM enrollments WHERE userId = :userId")
  suspend fun getActivityIds(userId: String): List<String>
}
