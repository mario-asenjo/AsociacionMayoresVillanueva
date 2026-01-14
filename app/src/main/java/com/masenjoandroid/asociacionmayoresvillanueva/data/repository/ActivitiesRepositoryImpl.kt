package com.masenjoandroid.asociacionmayoresvillanueva.data.repository

import com.masenjoandroid.asociacionmayoresvillanueva.common.AppResult
import com.masenjoandroid.asociacionmayoresvillanueva.data.firebase.FirebaseActivitiesDataSource
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem
import com.masenjoandroid.asociacionmayoresvillanueva.domain.repository.ActivitiesRepository

class ActivitiesRepositoryImpl(private val dataSource: FirebaseActivitiesDataSource) :
  ActivitiesRepository {

  override suspend fun getActivitiesForToday(): AppResult<List<ActivityItem>> = try {
    // Placeholder: cuando se implemente, mapear documentos reales.
    dataSource.fetchToday()
    AppResult.Success(emptyList())
  } catch (t: Throwable) {
    AppResult.Error("Error obteniendo actividades", t)
  }
}
