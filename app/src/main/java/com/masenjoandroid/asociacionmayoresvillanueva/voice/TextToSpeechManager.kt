package com.masenjoandroid.asociacionmayoresvillanueva.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import java.util.UUID

/**
 * Implementación real de [TextToSpeechEngine] usando [android.speech.tts.TextToSpeech].
 * - Idioma: es-ES
 * - Robustz: cola "pending" (último mensaje) antes de init
 * - API: stop() para cortar voz antes de escuchar con STT
 */
class TextToSpeechManager(context: Context) :
  TextToSpeechEngine,
  TextToSpeech.OnInitListener {

  private val appContext = context.applicationContext
  private var tts: TextToSpeech? = null

  @Volatile private var isInitialized: Boolean = false

  @Volatile private var pendingText: String? = null

  init {
    tts = TextToSpeech(appContext, this)
  }

  override fun onInit(status: Int) {
    if (status != TextToSpeech.SUCCESS) {
      Log.e(TAG, "Inicialización fallida (status=$status)")
      return
    }

    val engine = tts ?: return
    val locale = Locale.forLanguageTag("es-ES")
    val langResult = engine.setLanguage(locale)
    if (langResult == TextToSpeech.LANG_MISSING_DATA ||
      langResult == TextToSpeech.LANG_NOT_SUPPORTED
    ) {
      Log.e(TAG, "Idioma es-ES no soportado o faltan datos. Se usará Locale.getDefault()")
      engine.language = Locale.getDefault()
    }

    // Para personames mayores suele funcionar bien una velocidad ligeramente más lenta,
    // pero lo dejamos conservador por ahora.
    engine.setSpeechRate(1.0f)
    engine.setPitch(1.0f)
    isInitialized = true
    Log.i(TAG, "TTS listo!")

    // si hubo una petición antes de init, reproducimos la última.
    pendingText.let {
      pendingText = null
      speakInternal(it, TextToSpeech.QUEUE_FLUSH)
    }
  }

  override fun speak(text: String) {
    val clean = text.trim()
    if (clean.isBlank()) return

    if (!isInitialized || tts == null) {
      // Guardamos SOLO el último para evitar spam al arrancar
      pendingText = clean
      Log.i(TAG, "TTS aún no listo, guardo pendingText")
      return
    }

    speakInternal(clean, TextToSpeech.QUEUE_FLUSH)
  }

  override fun stop() {
    try {
      tts?.stop()
      Log.i(TAG, "TTS stop()")
    } catch (t: Throwable) {
      Log.e(TAG, "Error en stop()", t)
    }
  }

  override fun shutdown() {
    try {
      tts?.stop()
      tts?.shutdown()
      Log.i(TAG, "TTS Shutdown()")
    } catch (t: Throwable) {
      Log.e(TAG, "Error en shutdown()", t)
    } finally {
      tts = null
      isInitialized = false
      pendingText = null
    }
  }

  private fun speakInternal(text: String?, queueMode: Int) {
    val engine = tts ?: return
    val utteranceId = "utt_${UUID.randomUUID()}"

    val result = engine.speak(text, queueMode, null, utteranceId)
    if (result == TextToSpeech.ERROR) {
      Log.e(TAG, "TTS error al hablar (utterance=$utteranceId)")
    } else {
      Log.i(TAG, "TTS speaking: $text")
    }
  }

  private companion object {
    private const val TAG = "TTS"
  }
}
