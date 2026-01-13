package com.masenjo_android.asociacionmayoresvillanueva.agent

import com.masenjo_android.asociacionmayoresvillanueva.domain.model.ActivityItem

sealed class AgentResponse {
    data class ShowActivities(val list: List<ActivityItem>) : AgentResponse()
    data class ShowMessage(val text: String) : AgentResponse()
    data class ShowError(val text: String) : AgentResponse()
    data class AskForFields(val fieldsNeeded: List<String>) : AgentResponse()
}
