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

    private var speechRecognizer: SpeechRecognizer? = null
    private var recognitionIntent: Intent? = null

    init {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        recognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }
    }

    override fun startListening(onResult: (String) -> Unit, onError: (String) -> Unit) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("Reconocimiento de voz no disponible en este dispositivo.")
            return
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                    SpeechRecognizer.ERROR_CLIENT -> "Error del cliente"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permisos insuficientes"
                    SpeechRecognizer.ERROR_NETWORK -> "Error de red"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No se entendió nada"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                    else -> "Error desconocido ($error)"
                }
                Log.e("STT", "Error: $message")
                onError(message)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onResult(matches[0])
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer?.startListening(recognitionIntent)
    }

    override fun stopListening() {
        speechRecognizer?.stopListening()
    }

    override fun shutdown() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}
