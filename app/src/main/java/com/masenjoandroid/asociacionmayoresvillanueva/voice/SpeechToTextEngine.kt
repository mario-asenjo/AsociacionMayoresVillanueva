package com.masenjoandroid.asociacionmayoresvillanueva.voice

/**
 * Abstracción para el motor de Speech-To-Text.
 */
interface SpeechToTextEngine {
    /**
     * Inicia la escuchas.
     * @param onResult Callback que recibe el texto reconocido.
     * @param onError Callback para errores (opcional para simplificar).
     */
    fun startListening(onResult: (String) -> Unit, onError: (String) -> Unit)

    /**
     * Detiene la escucha activamente.
     */
    fun stopListening()

    /**
     * Libera recursos.
     */
    fun shutdown()
}
