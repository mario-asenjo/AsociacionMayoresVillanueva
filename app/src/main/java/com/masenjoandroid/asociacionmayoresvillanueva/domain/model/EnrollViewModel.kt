package com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechEngine

data class EnrollUiState(
  val activityTitle: String = "",
  val message: String = "",
  val finished: Boolean = false
)

class EnrollViewModel : ViewModel() {

  private val _state = MutableLiveData(EnrollUiState())
  val state: LiveData<EnrollUiState> = _state

  var ttsEngine: TextToSpeechEngine? = null

  fun setActivity(title: String) {
    val msg = "¿Quieres apuntarte a $title?"
    ttsEngine?.speak(msg)
    _state.value = EnrollUiState(activityTitle = title, message = msg)
  }

  fun confirmEnrollment() {
    val msg = "Te has inscrito correctamente"
    ttsEngine?.speak(msg)
    _state.value = _state.value?.copy(
      message = msg,
      finished = true
    )
  }
}
