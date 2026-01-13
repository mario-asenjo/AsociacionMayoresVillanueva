package com.masenjo_android.asociacionmayoresvillanueva.agent

class AgentOrchestrator(
  private val parser: AgentKeywordParser = AgentKeywordParser(),
  private val activitiesProvider: ActivitiesProvider = MockActivitiesProvider(),
) {

  fun handle(userText: String): AgentResponse {
    val trimmed = userText.trim()
    if (trimmed.isBlank()) {
      return AgentResponse.AskForFields(
        fieldsNeeded = listOf("Escribe o di qué necesitas (por ejemplo: “actividades hoy”)."),
      )
    }

    return when (val intent = parser.parse(trimmed)) {
      is AgentIntent.QueryActivities -> {
        val list = activitiesProvider.getActivities(intent.scope)
        if (list.isEmpty()) {
          AgentResponse.ShowMessage("No he encontrado actividades por ahora.")
        } else {
          AgentResponse.ShowActivities(list)
        }
      }

      is AgentIntent.CreateComplaint -> {
        AgentResponse.ShowMessage("Función en construcción… (pronto podrás enviar propuestas/quejas)")
      }

      is AgentIntent.RegisterToActivity -> {
        AgentResponse.ShowMessage("Inscripción/desinscripción: en construcción…")
      }

      is AgentIntent.UnregisterFromActivity -> {
        AgentResponse.ShowMessage("Inscripción/desinscripción: en construcción…")
      }

      is AgentIntent.ContactMonitor -> {
        AgentResponse.ShowMessage("Enviar correo a monitores: en construcción…")
      }

      is AgentIntent.RedeemRewards -> {
        AgentResponse.ShowMessage("Canje de experiencia por recompensas: en construcción…")
      }

      is AgentIntent.Unknown -> {
        AgentResponse.ShowMessage("No he entendido la petición todavía. Prueba con “actividades”.")
      }
    }
  }
}
