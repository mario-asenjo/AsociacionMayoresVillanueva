package com.masenjoandroid.asociacionmayoresvillanueva.agent

import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem

class AgentOrchestrator(private val parser: AgentKeywordParser = AgentKeywordParser()) {

  fun handle(text: String): AgentResponse {
    val intent = parser.parse(text)

    return when (intent) {
      is AgentIntent.QueryActivities -> {
        AgentResponse.ShowActivities(mockActivities(intent.scope))
      }

      is AgentIntent.CreateComplaint -> {
        AgentResponse.ShowMessage("Gracias. He registrado tu queja/propuesta (placeholder).")
      }

      is AgentIntent.RegisterToActivity -> {
        AgentResponse.ShowMessage("Te apunto a la actividad (placeholder).")
      }

      is AgentIntent.UnregisterFromActivity -> {
        AgentResponse.ShowMessage("Te doy de baja de la actividad (placeholder).")
      }

      is AgentIntent.ContactMonitor -> {
        AgentResponse.ShowMessage("Puedo contactar con un monitor (placeholder).")
      }

      is AgentIntent.RedeemRewards -> {
        AgentResponse.ShowMessage("Recompensas: en construcción…")
      }

      is AgentIntent.Unknown -> {
        if (text.isBlank()) {
          AgentResponse.AskForFields(listOf("Escribe algo, por ejemplo: “actividades hoy”."))
        } else {
          AgentResponse.ShowMessage("No he entendido: prueba con “actividades hoy/mañana”.")
        }
      }
    }
  }

  private fun mockActivities(scope: ActivitiesScope): List<ActivityItem> {
    val whenText = when (scope) {
      ActivitiesScope.TODAY -> "Hoy"
      ActivitiesScope.UPCOMING -> "Próximas"
      ActivitiesScope.REGISTERED -> "Mis inscritas"
      ActivitiesScope.AVAILABLE -> "Disponibles"
    }

    return listOf(
      ActivityItem(
        id = "a1",
        title = "Gimnasia suave ($whenText)",
        dateTime = "10:00",
        placeName = "Sala 1",
        tags = listOf("movilidad", "salud"),
        placePhotoUrl = null
      ),
      ActivityItem(
        id = "a2",
        title = "Paseo en grupo ($whenText)",
        dateTime = "12:00",
        placeName = "Parque",
        tags = listOf("cardio", "social"),
        placePhotoUrl = null
      ),
      ActivityItem(
        id = "a3",
        title = "Estiramientos ($whenText)",
        dateTime = "17:30",
        placeName = "Sala 2",
        tags = listOf("flexibilidad"),
        placePhotoUrl = null
      )
    )
  }
}
