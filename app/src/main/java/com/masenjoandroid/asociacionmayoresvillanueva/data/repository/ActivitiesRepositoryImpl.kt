package com.masenjoandroid.asociacionmayoresvillanueva.data.repository

import com.masenjoandroid.asociacionmayoresvillanueva.common.AppResult
import com.masenjoandroid.asociacionmayoresvillanueva.data.firebase.FirebaseActivitiesDataSource
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.Activity
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem
import com.masenjoandroid.asociacionmayoresvillanueva.domain.repository.ActivitiesRepository

class ActivitiesRepositoryImpl(private val dataSource: FirebaseActivitiesDataSource) :
  ActivitiesRepository {

  override suspend fun getActivitiesForToday(): AppResult<List<ActivityItem>> = try {
    val firebaseActivities: List<Activity> = dataSource.fetchActivities()

    val items = firebaseActivities.map { activity ->
      ActivityItem(
        id = activity.id,
        title = activity.nombre ?: "Actividad",
        dateTime = activity.fecha_hora ?: "",
        placeName = activity.ubicacion ?: "",
        placePhotoUrl = activity.imagen ?: "",
        tags = activity.etiquetas ?: emptyList()
      )
    }

    AppResult.Success(items)
  } catch (t: Throwable) {
    AppResult.Error("Error obteniendo actividades", t)
  }
}
