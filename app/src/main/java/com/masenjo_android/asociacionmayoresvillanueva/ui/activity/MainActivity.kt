package com.masenjo_android.asociacionmayoresvillanueva.ui.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.masenjo_android.asociacionmayoresvillanueva.databinding.ActivityMainBinding
import com.masenjo_android.asociacionmayoresvillanueva.ui.adapters.ActivitiesAdapter
import com.masenjo_android.asociacionmayoresvillanueva.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

  private val orchestrator = AgentOrchestrator()

  private val _uiModel = MutableStateFlow(MainUiModel())
  val uiModel: StateFlow<MainUiModel> = _uiModel.asStateFlow()

  fun onSendQuery(text: String) {
    // 1️⃣ Estado loading
    _uiModel.value = _uiModel.value.copy(
      isLoading = true,
      message = null,
    )

    val response = orchestrator.handle(text)

    when (response) {
      is AgentResponse.ShowActivities -> {
        _uiModel.value = MainUiModel(
          isLoading = false,
          items = response.list,
          message = "Mostrando actividades",
        )
      }

      is AgentResponse.ShowMessage -> {
        _uiModel.value = MainUiModel(
          isLoading = false,
          message = response.text,
        )
      }

      is AgentResponse.ShowError -> {
        _uiModel.value = MainUiModel(
          isLoading = false,
          message = "Error: ${response.text}",
        )
      }

      is AgentResponse.AskForFields -> {
        _uiModel.value = MainUiModel(
          isLoading = false,
          message = "Faltan datos: ${response.fieldsNeeded.joinToString()}",
        )
      }
    }
  }

  fun onSpeakClicked() {
    _uiModel.value = _uiModel.value.copy(
      message = "Funcionalidad de voz en construcción…",
    )
  }
}


  private fun observeState() {
    lifecycleScope.launch {
      viewModel.uiModel.collect { model ->

        // 1️⃣ Mensaje de estado
        binding.statusText.text = model.message ?: ""

        // 2️⃣ Loading
        binding.statusText.visibility =
          if (model.isLoading) android.view.View.VISIBLE else android.view.View.VISIBLE

        // 3️⃣ Lista
        adapter.submitList(model.items)

        // 4️⃣ Estado vacío
        if (!model.isLoading && model.items.isEmpty()) {
          binding.statusText.text = "No hay actividades disponibles"
        }
      }
    }
  }

}
