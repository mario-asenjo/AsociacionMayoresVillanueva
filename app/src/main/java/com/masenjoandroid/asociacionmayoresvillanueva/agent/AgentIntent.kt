package com.masenjoandroid.asociacionmayoresvillanueva.agent

sealed interface AgentIntent {

  data class QueryActivities(val scope: ActivitiesScope) : AgentIntent

  data class CreateComplaint(val rawText: String) : AgentIntent

  data class RegisterToActivity(val activityReference: String?) : AgentIntent

  data class UnregisterFromActivity(val activityReference: String?) : AgentIntent

  data class ContactMonitor(val rawText: String) : AgentIntent

  data class RedeemRewards(val rawText: String) : AgentIntent

  data class Unknown(val rawText: String) : AgentIntent
  data class CompleteActivity(val activityReference: String?) : AgentIntent
  data class OpenActivity(val activityReference: String?) : AgentIntent
}

enum class ActivitiesScope {
  TODAY,
  UPCOMING,
  REGISTERED,
  AVAILABLE
}
