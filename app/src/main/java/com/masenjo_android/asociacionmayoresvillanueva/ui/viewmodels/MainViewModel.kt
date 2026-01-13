package com.masenjo_android.asociacionmayoresvillanueva.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.masenjo_android.asociacionmayoresvillanueva.agent.AgentOrchestrator
import com.masenjo_android.asociacionmayoresvillanueva.agent.AgentResponse
import com.masenjo_android.asociacionmayoresvillanueva.domain.model.ActivityItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiModel(
    val items: List<ActivityItem> = emptyList(),
    val status: String = "Listo.",
)

class MainViewModel : ViewModel() {

    private val orchestrator = AgentOrchestrator()

    private val _uiModel = MutableStateFlow(MainUiModel())
    val uiModel: StateFlow<MainUiModel> = _uiModel.asStateFlow()

    fun onSendQuery(text: String) {
        val response = orchestrator.handle(text)

        when (response) {
            is AgentResponse.ShowActivities -> {
                _uiModel.value = _uiModel.value.copy(
                    items = response.list,
                    status = "Mostrando actividades (mock).",
                )
            }
            is AgentResponse.ShowMessage -> {
                _uiModel.value = _uiModel.value.copy(
                    status = response.text,
                )
            }
            is AgentResponse.ShowError -> {
                _uiModel.value = _uiModel.value.copy(
                    status = "Error: ${response.text}",
                )
            }
            is AgentResponse.AskForFields -> {
                _uiModel.value = _uiModel.value.copy(
                    status = response.fieldsNeeded.joinToString(" "),
                )
            }
        }
    }

    fun onSpeakClicked() {
        _uiModel.value = _uiModel.value.copy(
            status = "Hablar (voz): en construcción…",
        )
    }
}
