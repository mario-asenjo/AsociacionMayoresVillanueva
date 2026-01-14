package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.masenjoandroid.asociacionmayoresvillanueva.databinding.ActivityMainBinding
import com.masenjoandroid.asociacionmayoresvillanueva.ui.adapters.ActivitiesAdapter
import com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()

  private val adapter = ActivitiesAdapter(
    onClick = { item ->
      Toast.makeText(this, "Detalle (placeholder): ${item.title}", Toast.LENGTH_SHORT).show()
    }
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupUi()
    observeState()
    binding.statusText.text = "Pantalla cargada ✅"
  }

  private fun setupUi() {
    binding.resultsRecycler.layoutManager = LinearLayoutManager(this)
    binding.resultsRecycler.adapter = adapter

    // Si tienes ProgressBar "loading" en el layout, lo ocultamos siempre por ahora
    binding.loading?.let { it.visibility = android.view.View.GONE }

    binding.sendButton.setOnClickListener {
      sendQuery()
    }

    binding.speakButton.setOnClickListener {
      viewModel.onSpeakClicked()
    }

    binding.queryEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEND) {
        sendQuery()
        true
      } else {
        false
      }
    }
  }

  private fun sendQuery() {
    val text = binding.queryEditText.text?.toString().orEmpty()
    viewModel.onSendQuery(text)
  }

  private fun observeState() {
    lifecycleScope.launch {
      viewModel.uiModel.collect { model ->
        binding.statusText.text = model.status
        adapter.submitList(model.items)
      }
    }
  }
}
