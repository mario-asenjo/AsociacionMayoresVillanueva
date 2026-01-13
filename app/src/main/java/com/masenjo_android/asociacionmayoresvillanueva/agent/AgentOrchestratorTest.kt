package com.masenjo_android.asociacionmayoresvillanueva.agent

import org.junit.Assert.assertTrue
import org.junit.Test

class AgentOrchestratorTest {

    @Test
    fun `cuando contiene actividades devuelve ShowActivities`() {
        val orchestrator = AgentOrchestrator()
        val result = orchestrator.handle("actividades hoy")

        assertTrue(result is AgentResponse.ShowActivities)
    }

    @Test
    fun `cuando texto vacio pide campos`() {
        val orchestrator = AgentOrchestrator()
        val result = orchestrator.handle("   ")

        assertTrue(result is AgentResponse.AskForFields)
    }
}
