package com.masenjoandroid.asociacionmayoresvillanueva.agent

import java.text.Normalizer
import java.util.Locale

/**
 * Parser simple por palabras clave.
 * - Normaliza tildes (actividades/actividad, mañana/manana, etc.)
 * - Detecta intención principal + algunos matices.
 * - Evita lanzar excepciones: siempre devuelve un intent.
 */
class AgentKeywordParser {

  fun parse(userText: String): AgentIntent {
    val original = userText.trim()
    if (original.isBlank()) return AgentIntent.Unknown(original)

    val text = normalize(original)

    // 1) Actividades (consultar)
    if (containsAny(text, "actividad", "actividades", "rutina", "rutinas")) {
      val scope = when {
        containsAny(text, "hoy") -> ActivitiesScope.TODAY
        containsAny(
          text,
          "manana",
          "mañana",
          "proximas",
          "próximas",
          "semana"
        ) -> ActivitiesScope.UPCOMING
        containsAny(
          text,
          "inscritas",
          "inscrita",
          "apuntadas",
          "apuntada",
          "mis"
        ) -> ActivitiesScope.REGISTERED
        containsAny(text, "disponibles", "disponible") -> ActivitiesScope.AVAILABLE
        else -> ActivitiesScope.TODAY
      }
      return AgentIntent.QueryActivities(scope)
    }

    // 2) Quejas / propuestas
    if (containsAny(text, "queja", "propuesta", "sugerencia", "reclamacion", "reclamación")) {
      return AgentIntent.CreateComplaint(rawText = original)
    }

    // 3) Apuntarse / desapuntarse
    if (containsAny(text, "apunt", "inscrib", "registr", "alta")) {
      // No extraemos id aún (futuro: NER o UI selection)
      return AgentIntent.RegisterToActivity(activityReference = null)
    }

    if (containsAny(text, "desapunt", "baja", "cancel", "anular")) {
      return AgentIntent.UnregisterFromActivity(activityReference = null)
    }

    // 4) Correo a monitores
    if (containsAny(text, "correo", "email", "e-mail", "monitor", "monitores")) {
      return AgentIntent.ContactMonitor(rawText = original)
    }

    // 5) Recompensas / canje
    if (containsAny(text, "recompensa", "recompensas", "canje", "canjear", "premio", "premios")) {
      return AgentIntent.RedeemRewards(rawText = original)
    }

    return AgentIntent.Unknown(original)
  }

  private fun containsAny(text: String, vararg needles: String): Boolean =
    needles.any { text.contains(normalize(it)) }

  private fun normalize(input: String): String {
    val lower = input.lowercase(Locale("es", "ES"))
    val normalized = Normalizer.normalize(lower, Normalizer.Form.NFD)
    // Quita diacríticos
    return normalized.replace("\\p{Mn}+".toRegex(), "")
  }
}
