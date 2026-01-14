package com.masenjoandroid.asociacionmayoresvillanueva.voice

/**
 * Abstracción para el motor de Text-To-Speech.
 * Permite desacoplar la lógica de negocio de la implementación de Android.
 */
interface TextToSpeechEngine {
    /**
     * Pronuncia el texto indicado.
     */
    fun speak(text: String)

    /**
     * Libera los recursos del motor.
     */
    fun shutdown()
}
