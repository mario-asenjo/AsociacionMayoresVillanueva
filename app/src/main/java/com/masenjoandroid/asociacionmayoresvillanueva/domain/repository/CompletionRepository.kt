package com.masenjoandroid.asociacionmayoresvillanueva.domain.repository

import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.CompletionRequest

interface CompletionRepository {
  suspend fun submitCompletion(request: CompletionRequest)
  suspend fun getPendingForMonitor(): List<CompletionRequest> // stub simple por ahora
}
