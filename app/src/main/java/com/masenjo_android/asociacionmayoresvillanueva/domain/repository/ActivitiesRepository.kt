package com.masenjo_android.asociacionmayoresvillanueva.domain.repository

import com.masenjo_android.asociacionmayoresvillanueva.common.AppResult
import com.masenjo_android.asociacionmayoresvillanueva.domain.model.ActivityItem

interface ActivitiesRepository {
    suspend fun getActivitiesForToday(): AppResult<List<ActivityItem>>
}
