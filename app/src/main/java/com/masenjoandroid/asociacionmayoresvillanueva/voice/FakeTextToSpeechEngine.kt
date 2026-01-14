package com.masenjoandroid.asociacionmayoresvillanueva.voice

/**
 * Implementación falsa de [TextToSpeechEngine] para ser usada en tests unitarios.
 * No depende del framework de Android.
 */
class FakeTextToSpeechEngine : TextToSpeechEngine {

  var lastSpokenText: String? = null
    private set

  var isStopped: Boolean = false

  var isShutdown = false
    private set

  override fun speak(text: String) {
    isStopped = false
    lastSpokenText = text
  }

  override fun stop() {
    isStopped = true
  }

  override fun shutdown() {
    isShutdown = true
  }
}
