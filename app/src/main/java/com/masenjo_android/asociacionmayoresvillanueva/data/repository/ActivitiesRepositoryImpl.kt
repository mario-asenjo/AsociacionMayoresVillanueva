package com.masenjo_android.asociacionmayoresvillanueva.data.repository

import com.masenjo_android.asociacionmayoresvillanueva.common.AppResult
import com.masenjo_android.asociacionmayoresvillanueva.data.firebase.FirebaseActivitiesDataSource
import com.masenjo_android.asociacionmayoresvillanueva.domain.model.ActivityItem
import com.masenjo_android.asociacionmayoresvillanueva.domain.repository.ActivitiesRepository

class ActivitiesRepositoryImpl(
    private val dataSource: FirebaseActivitiesDataSource,
) : ActivitiesRepository {

    override suspend fun getActivitiesForToday(): AppResult<List<ActivityItem>> {
        return try {
            // Placeholder: cuando se implemente, mapear documentos reales.
            dataSource.fetchToday()
            AppResult.Success(emptyList())
        } catch (t: Throwable) {
            AppResult.Error("Error obteniendo actividades", t)
        }
    }
}
