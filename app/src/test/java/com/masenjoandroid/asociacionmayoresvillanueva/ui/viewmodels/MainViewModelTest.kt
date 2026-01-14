package com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels

import com.masenjoandroid.asociacionmayoresvillanueva.voice.FakeSpeechToTextEngine
import com.masenjoandroid.asociacionmayoresvillanueva.voice.FakeTextToSpeechEngine
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MainViewModelTest {

  @Test
  fun `onSendQuery actividades actualiza lista y canta resumen`() {
    val vm = MainViewModel()
    val fakeTts = FakeTextToSpeechEngine()
    vm.ttsEngine = fakeTts

    vm.onSendQuery("actividades hoy")

    val state = vm.uiModel.value
    assertTrue("Debería haber items", state.items.isNotEmpty())

    val spoken = fakeTts.lastSpokenText
    assertNotNull("Debería hablar algo", spoken)
    assertTrue("Debería mencionar actividades", spoken!!.contains("actividades", ignoreCase = true))
  }

  @Test
  fun `onSendQuery queja canta confirmacion`() {
    val vm = MainViewModel()
    val fakeTts = FakeTextToSpeechEngine()
    vm.ttsEngine = fakeTts

    vm.onSendQuery("quiero poner una queja")

    val spoken = fakeTts.lastSpokenText
    assertNotNull(spoken)
    assertTrue(spoken!!.isNotBlank())
  }

  @Test
  fun `onMicClicked inicia escucha y al recibir voz autoenvia y canta`() {
    val vm = MainViewModel()
    val fakeStt = FakeSpeechToTextEngine()
    val fakeTts = FakeTextToSpeechEngine()
    vm.sttEngine = fakeStt
    vm.ttsEngine = fakeTts

    vm.onMicClicked()
    assertTrue(fakeStt.isListening)

    // Simula dictado -> autoenvío
    fakeStt.simulateVoiceInput("actividades")

    val state = vm.uiModel.value
    assertTrue(state.items.isNotEmpty())

    val spoken = fakeTts.lastSpokenText
    assertNotNull(spoken)
    assertTrue(spoken!!.contains("actividades", ignoreCase = true))
  }
}
