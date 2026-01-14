package com.masenjoandroid.asociacionmayoresvillanueva.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

/**
 * Stub. Aquí irá SpeechRecognizer + permisos + callbacks.
 * La idea es que UI no conozca detalles de implementación.
 */
// (removed duplicate imports)

/**
 * Implementación real de [SpeechToTextEngine] usando [SpeechRecognizer].
 */
class SpeechToTextManager(private val context: Context) : SpeechToTextEngine {

  private val appContext = context.applicationContext
  private var speechRecognizer: SpeechRecognizer? = null
  private var recognitionIntent: Intent? = Intent(
    RecognizerIntent.ACTION_RECOGNIZE_SPEECH
  ).apply {
    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
  }

  @Volatile private var onResultCb: ((String) -> Unit)? = null

  @Volatile private var onErrorCb: ((String) -> Unit)? = null

  override fun startListening(onResult: (String) -> Unit, onError: (String) -> Unit) {
    if (!SpeechRecognizer.isRecognitionAvailable(appContext)) {
      onError("Reconocimiento de voz no disponible en este dispositivo.")
      return
    }

    onResultCb = onResult
    onErrorCb = onError

    if (speechRecognizer == null) {
      speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext).apply {
        setRecognitionListener(listener)
      }
    }

    speechRecognizer?.startListening(recognitionIntent)
  }

  override fun stopListening() {
    try {
      speechRecognizer?.stopListening()
    } catch (t: Throwable) {
      Log.e(TAG, "stopListening() error", t)
    }
  }

  override fun shutdown() {
    try {
      speechRecognizer?.destroy()
    } catch (t: Throwable) {
      Log.e(TAG, "shutdown() error", t)
    } finally {
      speechRecognizer = null
      onResultCb = null
      onErrorCb = null
    }
  }

  private val listener = object : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) { }
    override fun onBeginningOfSpeech() { }
    override fun onRmsChanged(rmsdB: Float) { }
    override fun onBufferReceived(buffer: ByteArray?) { }
    override fun onEndOfSpeech() { }

    override fun onError(error: Int) {
      val message = when (error) {
        SpeechRecognizer.ERROR_AUDIO -> "Problema con el audio."
        SpeechRecognizer.ERROR_CLIENT -> "Error interno del reconocimiento."
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Faltan permisos de micrófono."
        SpeechRecognizer.ERROR_NETWORK -> "Problema de red."
        SpeechRecognizer.ERROR_NO_MATCH -> "No he entendido lo que has dicho."
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "El micrófono está ocupado, inténtalo otra vez."
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se ha detectado voz."
        else -> "Error de voz ($error)."
      }
      Log.e(TAG, "STT error: $message")
      onErrorCb?.invoke(message)
    }

    override fun onResults(results: Bundle?) {
      val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
      val best = matches?.firstOrNull().orEmpty()
      if (best.isNotBlank()) {
        onResultCb?.invoke(best)
      } else {
        onErrorCb?.invoke("No he entendido lo que has dicho.")
      }
    }

    override fun onPartialResults(partialResults: Bundle?) { }
    override fun onEvent(eventType: Int, params: Bundle?) { }
  }

  private companion object {
    private const val TAG = "STT"
  }
}
