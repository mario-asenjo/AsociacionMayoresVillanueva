package com.masenjoandroid.asociacionmayoresvillanueva.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompletionRequestDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: CompletionRequestEntity)

  @Query("SELECT * FROM completion_requests WHERE userId = :userId ORDER BY createdAt DESC")
  suspend fun getByUser(userId: String): List<CompletionRequestEntity>

  @Query("SELECT * FROM completion_requests WHERE status = 'PENDING' ORDER BY createdAt DESC")
  suspend fun getPending(): List<CompletionRequestEntity>
}
