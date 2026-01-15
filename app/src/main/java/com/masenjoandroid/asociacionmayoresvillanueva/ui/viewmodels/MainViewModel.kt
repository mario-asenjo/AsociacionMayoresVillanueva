package com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.masenjoandroid.asociacionmayoresvillanueva.agent.AgentOrchestrator
import com.masenjoandroid.asociacionmayoresvillanueva.agent.AgentResponse
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem
import com.masenjoandroid.asociacionmayoresvillanueva.voice.SpeechToTextEngine
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechEngine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiModel(
  val items: List<ActivityItem> = emptyList(),
  val status: String = "Listo."
)

class MainViewModel : ViewModel() {

  sealed class MainUiEvent {
    data class OpenEnroll(val id: String, val title: String) : MainUiEvent()
    data class OpenComplete(val id: String, val title: String) : MainUiEvent()
  }

  private val orchestrator = AgentOrchestrator()

  var ttsEngine: TextToSpeechEngine? = null
  var sttEngine: SpeechToTextEngine? = null

  private val _events = MutableSharedFlow<MainUiEvent>(replay = 1, extraBufferCapacity = 1)
  val events: SharedFlow<MainUiEvent> = _events.asSharedFlow()

  private val _uiModel = MutableStateFlow(MainUiModel())
  val uiModel: StateFlow<MainUiModel> = _uiModel.asStateFlow()

  fun onSendQuery(text: String) {
    val response = orchestrator.handle(text)

    when (response) {
      is AgentResponse.ShowActivities -> {
        val list = response.list
        _uiModel.value = _uiModel.value.copy(
          items = list,
          status = if (list.isEmpty()) "No hay actividades para mostrar." else "Mostrando ${list.size} actividades."
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

      is AgentResponse.RequestEnroll -> {
        handleEnrollRequest(response.activityReference)
      }

      is AgentResponse.RequestComplete -> {
        handleCompleteRequest(response.activityReference)
      }
    }
  }

  fun onMicClicked() {
    ttsEngine?.stop()

    _uiModel.value = _uiModel.value.copy(status = "🎙️ Escuchando… Habla ahora.")

    sttEngine?.startListening(
      onResult = { spoken ->
        onSendQuery(spoken)
      },
      onError = { error ->
        _uiModel.value = _uiModel.value.copy(status = "🎙️ $error")
        speak(error)
      }
    )
  }

  private fun handleEnrollRequest(reference: String?) {
    val items = _uiModel.value.items
    if (items.isEmpty()) {
      val msg = "Primero pide la lista: di por ejemplo “actividades hoy”."
      _uiModel.value = _uiModel.value.copy(status = msg)
      speak(msg)
      return
    }

    val idx = reference?.toIntOrNull()?.minus(1)
    if (idx == null) {
      val msg = "¿A cuál actividad? Di “la primera”, “la segunda” o “la tercera”."
      _uiModel.value = _uiModel.value.copy(status = msg)
      speak(msg)
      return
    }

    val item = items.getOrNull(idx)
    if (item == null) {
      val msg = "No existe la actividad número ${idx + 1}. Prueba con un número entre 1 y ${items.size}."
      _uiModel.value = _uiModel.value.copy(status = msg)
      speak(msg)
      return
    }

    val msg = "Abriendo inscripción para: ${item.title}."
    _uiModel.value = _uiModel.value.copy(status = msg)
    speak(msg)

    _events.tryEmit(MainUiEvent.OpenEnroll(item.id, item.title))
  }

  private fun handleCompleteRequest(reference: String?) {
    val items = _uiModel.value.items
    if (items.isEmpty()) {
      val msg = "Primero pide la lista: di por ejemplo “actividades hoy”."
      _uiModel.value = _uiModel.value.copy(status = msg)
      speak(msg)
      return
    }

    val idx = reference?.toIntOrNull()?.minus(1)
    if (idx == null) {
      val msg = "¿Qué actividad quieres completar? Di “la primera”, “la segunda” o “la tercera”."
      _uiModel.value = _uiModel.value.copy(status = msg)
      speak(msg)
      return
    }

    val item = items.getOrNull(idx)
    if (item == null) {
      val msg = "No existe la actividad número ${idx + 1}. Prueba con un número entre 1 y ${items.size}."
      _uiModel.value = _uiModel.value.copy(status = msg)
      speak(msg)
      return
    }

    val msg = "Vamos a completar: ${item.title}."
    _uiModel.value = _uiModel.value.copy(status = msg)
    speak(msg)

    _events.tryEmit(MainUiEvent.OpenComplete(item.id, item.title))
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
