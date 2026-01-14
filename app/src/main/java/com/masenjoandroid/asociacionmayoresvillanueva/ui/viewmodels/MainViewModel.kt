package com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.masenjoandroid.asociacionmayoresvillanueva.agent.AgentOrchestrator
import com.masenjoandroid.asociacionmayoresvillanueva.agent.AgentResponse
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem
import com.masenjoandroid.asociacionmayoresvillanueva.voice.SpeechToTextEngine
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiModel(val items: List<ActivityItem> = emptyList(), val status: String = "Listo.")

class MainViewModel : ViewModel() {

  private val orchestrator = AgentOrchestrator()

  var ttsEngine: TextToSpeechEngine? = null
  var sttEngine: SpeechToTextEngine? = null

  private val _uiModel = MutableStateFlow(MainUiModel())
  val uiModel: StateFlow<MainUiModel> = _uiModel.asStateFlow()

  fun onSendQuery(text: String) {
    val response = orchestrator.handle(text)

    when (response) {
      is AgentResponse.ShowActivities -> {
        val list = response.list
        _uiModel.value = _uiModel.value.copy(
          items = list,
          status = if (list.isEmpty()) {
            "No hay actividades para mostrar."
          } else {
            "Mostrando ${list.size} actividades."
          }
        )
        speakActivities(list)
      }
      is AgentResponse.ShowMessage -> {
        _uiModel.value = _uiModel.value.copy(status = response.text)
        speak(response.text)
      }
      is AgentResponse.ShowError -> {
        val msg = "Error: ${response.text}"
        _uiModel.value = _uiModel.value.copy(status = msg)
        speak(msg)
      }
      is AgentResponse.AskForFields -> {
        val msg = response.fieldsNeeded.joinToString(" ")
        _uiModel.value = _uiModel.value.copy(status = msg)
        speak(msg)
      }
    }
  }

  fun onMicClicked() {
    // Paramos TTS antes de escuchar para que no interfieran.
    ttsEngine?.stop()

    _uiModel.value = _uiModel.value.copy(
      status = "🎙️ Escuchando… Habla ahora."
    )

    sttEngine?.startListening(
      onResult = { text ->
        // Voice-First: auto-envía la query reconocida
        onSendQuery(text)
      },
      onError = { error ->
        _uiModel.value = _uiModel.value.copy(
          status = "🎙️ $error"
        )
        // En errores de voz, también lo leemos en voz alta (opcional).
        // Si se prefiere no hablar errores cuando el micro falla, loquitamos.
        speak(error)
      }
    )
  }

  private fun speak(text: String) {
    ttsEngine?.speak(text)
  }

  private fun speakActivities(list: List<ActivityItem>) {
    if (list.isEmpty()) {
      speak("No hay actividades disponibles ahora mismo.")
      return
    }

    val top = list.take(3)
    val details = top.mapIndexed { idx, item ->
      val time = item.dateTime.takeIf { it.isNotBlank() } ?: "sin hora"
      val place = item.placeName.takeIf { it.isNotBlank() } ?: "sin ubicación"
      "${idx + 1}. ${item.title}. A las $time, en $place."
    }.joinToString(" ")
    val more = if (list.size > 3) "Y ${list.size - 3} más." else ""

    speak("He encontrado ${list.size} actividades. $details $more")
  }
}
