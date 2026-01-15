package com.masenjoandroid.asociacionmayoresvillanueva.voice

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class TextToSpeechManager(context: Context) :
  TextToSpeechEngine,
  TextToSpeech.OnInitListener {

  private val appContext = context.applicationContext
  private val mainHandler = Handler(Looper.getMainLooper())

  private var tts: TextToSpeech? = null

  @Volatile private var isInitialized: Boolean = false
  @Volatile private var pendingText: String? = null
  @Volatile private var pendingOnDone: (() -> Unit)? = null

  private val onDoneCallbacks = ConcurrentHashMap<String, () -> Unit>()

  init {
    tts = TextToSpeech(appContext, this)
  }

  override fun onInit(status: Int) {
    if (status != TextToSpeech.SUCCESS) {
      Log.e(TAG, "Inicialización fallida (status=$status)")
      return
    }

    val engine = tts ?: return

    engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
      override fun onStart(utteranceId: String?) = Unit

      override fun onDone(utteranceId: String?) {
        if (utteranceId == null) return
        val cb = onDoneCallbacks.remove(utteranceId) ?: return
        mainHandler.post { cb.invoke() }
      }

      @Deprecated("Deprecated in Java")
      override fun onError(utteranceId: String?) {
        if (utteranceId == null) return
        onDoneCallbacks.remove(utteranceId)
      }

      override fun onError(utteranceId: String?, errorCode: Int) {
        if (utteranceId == null) return
        onDoneCallbacks.remove(utteranceId)
      }
    })

    val locale = Locale.forLanguageTag("es-ES")
    val langResult = engine.setLanguage(locale)
    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
      Log.e(TAG, "Idioma es-ES no soportado, usando Locale.getDefault()")
      engine.language = Locale.getDefault()
    }

    engine.setSpeechRate(1.0f)
    engine.setPitch(1.0f)

    isInitialized = true
    Log.i(TAG, "TTS listo!")

    val text = pendingText
    val cb = pendingOnDone
    pendingText = null
    pendingOnDone = null

    if (!text.isNullOrBlank()) {
      speak(text, cb ?: {})
    }
  }

  override fun speak(text: String) {
    speak(text) {}
  }

  override fun speak(text: String, onDone: () -> Unit) {
    val clean = text.trim()
    if (clean.isBlank()) return

    if (!isInitialized || tts == null) {
      pendingText = clean
      pendingOnDone = onDone
      return
    }

    speakInternal(clean, TextToSpeech.QUEUE_FLUSH, onDone)
  }

  override fun stop() {
    try {
      tts?.stop()
    } catch (t: Throwable) {
      Log.e(TAG, "Error en stop()", t)
    }
  }

  override fun shutdown() {
    try {
      tts?.stop()
      tts?.shutdown()
    } catch (t: Throwable) {
      Log.e(TAG, "Error en shutdown()", t)
    } finally {
      tts = null
      isInitialized = false
      pendingText = null
      pendingOnDone = null
      onDoneCallbacks.clear()
    }
  }

  private fun speakInternal(text: String, queueMode: Int, onDone: () -> Unit) {
    val engine = tts ?: return
    val utteranceId = "utt_${UUID.randomUUID()}"
    onDoneCallbacks[utteranceId] = onDone

    val result = engine.speak(text, queueMode, null, utteranceId)
    if (result == TextToSpeech.ERROR) {
      onDoneCallbacks.remove(utteranceId)
      Log.e(TAG, "TTS error al hablar (utterance=$utteranceId)")
    }
  }

  private companion object {
    private const val TAG = "TTS"
  }
}
