package com.masenjoandroid.asociacionmayoresvillanueva.data.repository

import com.masenjoandroid.asociacionmayoresvillanueva.data.local.CompletionRequestDao
import com.masenjoandroid.asociacionmayoresvillanueva.data.local.CompletionRequestEntity
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.CompletionRequest
import com.masenjoandroid.asociacionmayoresvillanueva.domain.repository.CompletionRepository
import java.util.UUID

class CompletionRepositoryImpl(private val dao: CompletionRequestDao) : CompletionRepository {

  override suspend fun submitCompletion(request: CompletionRequest) {
    val entity = CompletionRequestEntity(
      id = request.id.ifBlank { UUID.randomUUID().toString() },
      userId = request.userId,
      activityId = request.activityId,
      rating = 0, // se completará desde el formulario real
      difficultyText = "",
      mediaUri = null,
      status = request.status,
      createdAt = System.currentTimeMillis()
    )
    dao.insert(entity)
  }

  override suspend fun getPendingForMonitor(): List<CompletionRequest> =
    dao.getPending().map {
      CompletionRequest(
        id = it.id,
        activityId = it.activityId,
        userId = it.userId,
        durationMinutes = 0,
        status = it.status
      )
    }
}
