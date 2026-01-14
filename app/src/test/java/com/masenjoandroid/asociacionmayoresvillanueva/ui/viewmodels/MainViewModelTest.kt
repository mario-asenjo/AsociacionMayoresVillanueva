package com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels

import com.masenjoandroid.asociacionmayoresvillanueva.voice.FakeSpeechToTextEngine
import com.masenjoandroid.asociacionmayoresvillanueva.voice.FakeTextToSpeechEngine
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MainViewModelTest {

  @Test
  fun `onSpeakClicked uses tts engine`() {
    val viewModel = MainViewModel()
    val fakeEngine = FakeTextToSpeechEngine()
    viewModel.ttsEngine = fakeEngine

    // Estado inicial
    val initialStatus = viewModel.uiModel.value.status // "Listo."

    viewModel.onMicClicked()

    // Verificamos que se llamó al motor
    assertEquals(initialStatus, fakeEngine.lastSpokenText)

    // Verificamos que el estado visual cambió
    assertEquals("Hablando: $initialStatus", viewModel.uiModel.value.status)
  }

  @Test
  fun `onSpeakClicked without engine does not crash`() {
    val viewModel = MainViewModel()
    // No asignamos engine

    viewModel.onMicClicked()

    // Verificamos que cambia el estado aunque no hable
    val expectedStatus = "Hablando: Listo."
    assertEquals(expectedStatus, viewModel.uiModel.value.status)
  }

  @Test
  fun `onMicClicked starts listening and processes result`() {
    val viewModel = MainViewModel()
    val fakeStt = FakeSpeechToTextEngine()
    viewModel.sttEngine = fakeStt

    // 1. Iniciar escucha
    viewModel.onMicClicked()
    assert(fakeStt.isListening)

    // 2. Simular voz: "actividades"
    // Sabemos que "actividades" -> devuelve mock de actividades y status "Mostrando actividades (mock)."
    fakeStt.simulateVoiceInput("actividades")

    // 3. Verificar resultado
    val state = viewModel.uiModel.value
    assertEquals("Mostrando actividades (mock).", state.status)
    assert(state.items.isNotEmpty())
  }
}
