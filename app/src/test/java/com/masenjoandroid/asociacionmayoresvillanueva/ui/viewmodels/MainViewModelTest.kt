package com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels

import com.masenjoandroid.asociacionmayoresvillanueva.voice.FakeSpeechToTextEngine
import com.masenjoandroid.asociacionmayoresvillanueva.voice.FakeTextToSpeechEngine
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
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

    fakeStt.simulateVoiceInput("actividades")

    val state = vm.uiModel.value
    assertTrue(state.items.isNotEmpty())

    val spoken = fakeTts.lastSpokenText
    assertNotNull(spoken)
    assertTrue(spoken!!.contains("actividades", ignoreCase = true))
  }

  @Test
  fun `apuntame a la segunda emite evento OpenEnroll`() = runBlocking {
    val vm = MainViewModel()
    vm.ttsEngine = FakeTextToSpeechEngine()

    vm.onSendQuery("actividades hoy")
    assertTrue(vm.uiModel.value.items.isNotEmpty())

    // ✅ Suscripción inmediata ANTES de emitir (UNDISPATCHED)
    val eventDeferred =
      async(start = CoroutineStart.UNDISPATCHED) {
        vm.events.first()
      }

    vm.onSendQuery("apuntame a la segunda")

    val event = withTimeout(2.seconds) { eventDeferred.await() }
    val open = event as MainViewModel.MainUiEvent.OpenEnroll
    assertEquals("a2", open.id)
  }

  @Test
  fun `abre la segunda con lista cargada abre enroll`() = runBlocking {
    val vm = MainViewModel()
    vm.ttsEngine = FakeTextToSpeechEngine()

    vm.onSendQuery("actividades hoy")
    assertTrue(vm.uiModel.value.items.isNotEmpty())

    val eventDeferred =
      async(start = CoroutineStart.UNDISPATCHED) {
        vm.events.first()
      }

    vm.onSendQuery("abre la segunda")

    val event = withTimeout(2.seconds) { eventDeferred.await() }
    val open = event as MainViewModel.MainUiEvent.OpenEnroll
    assertEquals("a2", open.id)
  }

  @Test
  fun `abre la segunda sin lista previa primero carga actividades y luego abre enroll`() =
    runBlocking {
      val vm = MainViewModel()
      vm.ttsEngine = FakeTextToSpeechEngine()

      assertTrue(vm.uiModel.value.items.isEmpty())

      val eventDeferred =
        async(start = CoroutineStart.UNDISPATCHED) {
          vm.events.first()
        }

      vm.onSendQuery("abre la segunda")

      // Debe haber cargado lista por el flujo interno (pendingOpenReference)
      assertTrue("Debería haber cargado la lista", vm.uiModel.value.items.isNotEmpty())

      val event = withTimeout(2.seconds) { eventDeferred.await() }
      val open = event as MainViewModel.MainUiEvent.OpenEnroll
      assertEquals("a2", open.id)
    }

  @Test
  fun `evento no se re-emite al volver a coleccionar`() = runBlocking {
    val vm = MainViewModel()
    vm.ttsEngine = FakeTextToSpeechEngine()

    vm.onSendQuery("actividades hoy")

    val firstEventDeferred =
      async(start = CoroutineStart.UNDISPATCHED) {
        vm.events.first()
      }

    vm.onSendQuery("apuntame a la primera")

    val firstEvent = withTimeout(2.seconds) { firstEventDeferred.await() }
    assertTrue(firstEvent is MainViewModel.MainUiEvent.OpenEnroll)

    // ✅ Con replay=0, una nueva suscripción NO debería recibir nada
    val second = withTimeoutOrNull(300.milliseconds) { vm.events.first() }
    assertEquals(null, second)
  }
}
