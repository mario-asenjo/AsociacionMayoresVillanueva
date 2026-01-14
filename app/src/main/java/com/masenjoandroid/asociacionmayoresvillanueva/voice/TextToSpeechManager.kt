package com.masenjoandroid.asociacionmayoresvillanueva.voice

import android.content.Context

import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Implementación real de [TextToSpeechEngine] usando [android.speech.tts.TextToSpeech].
 */
class TextToSpeechManager(context: Context) : TextToSpeechEngine, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val pendingText = mutableListOf<String>()

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "ES"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "El idioma no es soportado")
            } else {
                isInitialized = true
                processPendingQueue()
            }
        } else {
            Log.e("TTS", "Inicialización fallida")
        }
    }

    override fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            pendingText.add(text)
        }
    }

    private fun processPendingQueue() {
        for (text in pendingText) {
            speak(text)
        }
        pendingText.clear()
    }

    override fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
