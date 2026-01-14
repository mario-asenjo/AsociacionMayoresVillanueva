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

data class MainUiModel(
  val items: List<ActivityItem> = emptyList(),
  val status: String = "Listo."
)

class MainViewModel : ViewModel() {

  private val orchestrator = AgentOrchestrator()

  var ttsEngine: TextToSpeechEngine? = null
  var sttEngine: SpeechToTextEngine? = null

  private var pendingEnrollment: ActivityItem? = null

  private val _uiModel = MutableStateFlow(MainUiModel())
  val uiModel: StateFlow<MainUiModel> = _uiModel.asStateFlow()

  fun onSendQuery(text: String) {
    val response = orchestrator.handle(text)

    when (response) {
      is AgentResponse.ShowActivities -> {
        _uiModel.value = _uiModel.value.copy(
          items = response.list,
          status = "Mostrando actividades."
        )
      }
      is AgentResponse.ShowMessage -> {
        _uiModel.value = _uiModel.value.copy(status = response.text)
      }
      is AgentResponse.ShowError -> {
        _uiModel.value = _uiModel.value.copy(
          status = "Error: ${response.text}"
        )
      }
      is AgentResponse.AskForFields -> {
        _uiModel.value = _uiModel.value.copy(
          status = response.fieldsNeeded.joinToString(" ")
        )
      }
    }
  }

  fun onActivitySelected(activity: ActivityItem) {
    pendingEnrollment = activity
    val question = "¿Quieres apuntarte a ${activity.title}?"
    _uiModel.value = _uiModel.value.copy(status = question)
    ttsEngine?.speak(question)
  }

  fun onMicClicked() {
    sttEngine?.startListening(
      onResult = { text ->
        val normalized = text.lowercase()

        when {
          pendingEnrollment != null && normalized.contains("sí") -> {
            val activity = pendingEnrollment!!
            pendingEnrollment = null

            val confirmation = "Te has apuntado a ${activity.title}"
            _uiModel.value = _uiModel.value.copy(status = confirmation)
            ttsEngine?.speak(confirmation)
          }

          pendingEnrollment != null && normalized.contains("no") -> {
            pendingEnrollment = null
            val cancel = "De acuerdo, no te apunto"
            _uiModel.value = _uiModel.value.copy(status = cancel)
            ttsEngine?.speak(cancel)
          }

          else -> {
            onSendQuery(text)
          }
        }
      },
      onError = { error ->
        _uiModel.value = _uiModel.value.copy(
          status = "Error de voz: $error"
        )
      }
    )
  }
}
