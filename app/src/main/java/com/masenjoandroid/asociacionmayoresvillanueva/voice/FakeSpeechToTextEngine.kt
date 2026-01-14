package com.masenjoandroid.asociacionmayoresvillanueva.voice

/**
 * Implementación falsa de [SpeechToTextEngine] para tests.
 */
class FakeSpeechToTextEngine : SpeechToTextEngine {

  private var callback: ((String) -> Unit)? = null
  var isListening = false
    private set

  override fun startListening(onResult: (String) -> Unit, onError: (String) -> Unit) {
    isListening = true
    callback = onResult
  }

  override fun stopListening() {
    isListening = false
  }

  override fun shutdown() {
    isListening = false
    callback = null
  }

  // Método helper para simular entrada de voz en tests
  fun simulateVoiceInput(text: String) {
    if (isListening) {
      callback?.invoke(text)
    }
  }
}
