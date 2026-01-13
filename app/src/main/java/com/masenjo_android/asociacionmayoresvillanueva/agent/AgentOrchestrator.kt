package com.masenjo_android.asociacionmayoresvillanueva.agent

import com.masenjo_android.asociacionmayoresvillanueva.domain.model.ActivityItem

class AgentOrchestrator {

    fun handle(userText: String): AgentResponse {
        val normalized = userText.trim().lowercase()

        if (normalized.isBlank()) {
            return AgentResponse.AskForFields(
                fieldsNeeded = listOf("Escribe o di qué necesitas (por ejemplo: “actividades hoy”)"),
            )
        }

        return when {
            normalized.contains("actividades") || normalized.contains("actividad") -> {
                AgentResponse.ShowActivities(mockActivities())
            }
            normalized.contains("queja") || normalized.contains("propuesta") -> {
                AgentResponse.ShowMessage("Función en construcción… (pronto podrás enviar propuestas/quejas)")
            }
            normalized.contains("apunt") || normalized.contains("inscrib") -> {
                AgentResponse.ShowMessage("Inscripción/desinscripción: en construcción…")
            }
            normalized.contains("correo") || normalized.contains("email") -> {
                AgentResponse.ShowMessage("Enviar correo a monitores: en construcción…")
            }
            normalized.contains("recomp") || normalized.contains("canje") -> {
                AgentResponse.ShowMessage("Canje de experiencia por recompensas: en construcción…")
            }
            else -> {
                AgentResponse.ShowMessage("No he entendido la petición todavía. Prueba con “actividades”.")
            }
        }
    }

    private fun mockActivities(): List<ActivityItem> {
        return listOf(
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
    }
}
