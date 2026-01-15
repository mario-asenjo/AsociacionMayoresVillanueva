package com.masenjoandroid.asociacionmayoresvillanueva.agent

import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem

sealed class AgentResponse {
  data class ShowActivities(val list: List<ActivityItem>) : AgentResponse()
  data class ShowMessage(val text: String) : AgentResponse()
  data class ShowError(val text: String) : AgentResponse()
  data class AskForFields(val fieldsNeeded: List<String>) : AgentResponse()
  data class RequestEnroll(val activityReference: String?) : AgentResponse()
  data class RequestComplete(val activityReference: String?) : AgentResponse()
}
