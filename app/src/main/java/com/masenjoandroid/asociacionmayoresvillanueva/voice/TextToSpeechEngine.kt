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
   * Para cualquier locución en curso (sin liberar recursos).
   */
  fun stop()

  /**
   * Libera los recursos del motor.
   */
  fun shutdown()

  // NUEVO: callback cuando termina de hablar
  fun speak(text: String, onDone: () -> Unit) {
    speak(text) // por defecto no hay callback si el engine no lo implementa
  }
}
