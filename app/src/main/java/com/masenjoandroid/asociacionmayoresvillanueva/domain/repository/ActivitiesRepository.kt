package com.masenjoandroid.asociacionmayoresvillanueva.domain.repository

import com.masenjoandroid.asociacionmayoresvillanueva.common.AppResult
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem

interface ActivitiesRepository {
  suspend fun getActivitiesForToday(): AppResult<List<ActivityItem>>
}
