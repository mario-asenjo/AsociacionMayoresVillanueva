package com.masenjoandroid.asociacionmayoresvillanueva.agent

import junit.framework.TestCase.assertTrue
import org.junit.Test

class AgentOrchestratorTest {

  @Test
  fun `actividades devuelve ShowActivities`() {
    val orchestrator = AgentOrchestrator()
    val result = orchestrator.handle("actividades hoy")
    assertTrue(result is AgentResponse.ShowActivities)
  }

  @Test
  fun `actividades con tilde o sin tilde funciona`() {
    val orchestrator = AgentOrchestrator()
    val result1 = orchestrator.handle("actividades mañana")
    val result2 = orchestrator.handle("actividades manana")
    assertTrue(result1 is AgentResponse.ShowActivities)
    assertTrue(result2 is AgentResponse.ShowActivities)
  }

  @Test
  fun `queja devuelve ShowMessage`() {
    val orchestrator = AgentOrchestrator()
    val result = orchestrator.handle("quiero poner una queja")
    assertTrue(result is AgentResponse.ShowMessage)
  }

  @Test
  fun `texto vacio pide campos`() {
    val orchestrator = AgentOrchestrator()
    val result = orchestrator.handle("   ")
    assertTrue(result is AgentResponse.AskForFields)
  }
}
