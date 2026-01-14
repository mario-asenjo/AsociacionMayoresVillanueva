package com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.masenjoandroid.asociacionmayoresvillanueva.agent.AgentOrchestrator
import com.masenjoandroid.asociacionmayoresvillanueva.agent.AgentResponse
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechEngine
import com.masenjoandroid.asociacionmayoresvillanueva.voice.SpeechToTextEngine

data class MainUiModel(val items: List<ActivityItem> = emptyList(), val status: String = "Listo.")

class MainViewModel : ViewModel() {

  private val orchestrator = AgentOrchestrator()

 // Setter injection para evitar refactorizar toda la DI de golpe
  var ttsEngine: TextToSpeechEngine? = null
  var sttEngine: SpeechToTextEngine? = null

  private val _uiModel = MutableStateFlow(MainUiModel())
  val uiModel: StateFlow<MainUiModel> = _uiModel.asStateFlow()

  fun onSendQuery(text: String) {
    val response = orchestrator.handle(text)

    when (response) {
      is AgentResponse.ShowActivities -> {
        _uiModel.value = _uiModel.value.copy(
          items = response.list,
          status = "Mostrando actividades (mock)."
        )
      }
      is AgentResponse.ShowMessage -> {
        _uiModel.value = _uiModel.value.copy(
          status = response.text
        )
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

  fun onSpeakClicked() {
    val textToSpeak = _uiModel.value.status
    // Hablamos el estado actual si hay motor disponible
    ttsEngine?.speak(textToSpeak)
    
    _uiModel.value = _uiModel.value.copy(
      status = "Hablando: $textToSpeak"
    )
  }

  fun onMicClicked() {
      // Escuchar
      sttEngine?.startListening(
          onResult = { text ->
              // Cuando hay resultado, enviamos la query
              onSendQuery(text)
          },
          onError = { error ->
              _uiModel.value = _uiModel.value.copy(
                  status = "Error voz: $error"
              )
          }
      )
  }
}
