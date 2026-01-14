package com.masenjoandroid.asociacionmayoresvillanueva.voice

import android.content.Context

/**
 * Stub. Aquí irá SpeechRecognizer + permisos + callbacks.
 * La idea es que UI no conozca detalles de implementación.
 */
class SpeechToTextManager(private val context: Context) {
  interface Listener {
    fun onPartialResult(text: String)
    fun onFinalResult(text: String)
    fun onError(message: String)
  }

  fun startListening(listener: Listener) {
    // Placeholder: no implementado
    listener.onError("Reconocimiento de voz: en construcción…")
  }

  fun stopListening() {
    // Placeholder
  }
}
