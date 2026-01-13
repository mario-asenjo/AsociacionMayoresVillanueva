package com.masenjo_android.asociacionmayoresvillanueva.agent

import com.masenjo_android.asociacionmayoresvillanueva.domain.model.ActivityItem

/**
 * Punto de extensión: hoy se usa mock.
 * Mañana puedes reemplazar esto por una implementación que consulte repositorios/Firebase.
 */
fun interface ActivitiesProvider {
  fun getActivities(scope: ActivitiesScope): List<ActivityItem>
}

class MockActivitiesProvider : ActivitiesProvider {
  override fun getActivities(scope: ActivitiesScope): List<ActivityItem> {
    // Mock sencillo. En futuro, scope filtrará de verdad.
    val base = listOf(
      ActivityItem(
        id = "a1",
        title = "Paseo suave por el parque",
        dateTime = "Hoy · 10:00",
        placeName = "Parque Municipal",
        placePhotoUrl = null,
        tags = listOf("suave", "exterior", "grupo"),
      ),
      ActivityItem(
        id = "a2",
        title = "Movilidad articular en sala",
        dateTime = "Hoy · 12:30",
        placeName = "Sala principal",
        placePhotoUrl = null,
        tags = listOf("interior", "bajo impacto"),
      ),
      ActivityItem(
        id = "a3",
        title = "Estiramientos guiados",
        dateTime = "Mañana · 09:30",
        placeName = "Sala multiusos",
        placePhotoUrl = null,
        tags = listOf("flexibilidad", "suave"),
      ),
    )

    return when (scope) {
      ActivitiesScope.TODAY -> base.filter { it.dateTime.startsWith("Hoy") }
      ActivitiesScope.UPCOMING -> base
      ActivitiesScope.REGISTERED -> base.take(1) // placeholder
      ActivitiesScope.AVAILABLE -> base
    }
  }
}
