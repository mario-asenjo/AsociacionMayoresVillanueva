package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.masenjoandroid.asociacionmayoresvillanueva.ui.adapters.ActivitiesAdapter
import com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ActivityMainBinding
import com.masenjoandroid.asociacionmayoresvillanueva.voice.SpeechToTextManager
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechManager

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()

  // Mantenemos referencia para el ciclo de vida
  private lateinit var ttsManager: TextToSpeechManager
  private lateinit var sttManager: SpeechToTextManager

  private val requestPermissionLauncher =
      registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
          if (isGranted) {
              viewModel.onMicClicked()
          } else {
              Toast.makeText(this, "Permiso de micrófono necesario", Toast.LENGTH_SHORT).show()
          }
      }

  private val adapter = ActivitiesAdapter(
    onClick = { item ->
      Toast.makeText(this, "Detalle (placeholder): ${item.title}", Toast.LENGTH_SHORT).show()
    }
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Inicializar TTS y STT
    ttsManager = TextToSpeechManager(this)
    sttManager = SpeechToTextManager(this)

    // Inyectar en ViewModel
    viewModel.ttsEngine = ttsManager
    viewModel.sttEngine = sttManager

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupUi()
    observeState()
    binding.statusText.text = "Pantalla cargada ✅"
  }

  override fun onDestroy() {
      super.onDestroy()
      if (::ttsManager.isInitialized) {
          ttsManager.shutdown()
      }
      if (::sttManager.isInitialized) {
          sttManager.shutdown()
      }
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
      checkAudioPermissionAndListen()
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

  private fun checkAudioPermissionAndListen() {
      when {
          ContextCompat.checkSelfPermission(
              this,
              Manifest.permission.RECORD_AUDIO
          ) == PackageManager.PERMISSION_GRANTED -> {
              viewModel.onMicClicked()
          }
          else -> {
              requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
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
