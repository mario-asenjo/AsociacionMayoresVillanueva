package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ActivityMainBinding
import com.masenjoandroid.asociacionmayoresvillanueva.ui.adapters.ActivitiesAdapter
import com.masenjoandroid.asociacionmayoresvillanueva.ui.viewmodels.MainViewModel
import com.masenjoandroid.asociacionmayoresvillanueva.voice.SpeechToTextManager
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()

  private lateinit var ttsManager: TextToSpeechManager
  private lateinit var sttManager: SpeechToTextManager

  private val requestPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
      if (isGranted) {
        viewModel.onMicClicked()
      } else {
        Toast.makeText(this, "Permiso de micrófono necesario", Toast.LENGTH_SHORT).show()
      }
    }

  private val adapter = ActivitiesAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Inicializamos TTS y STT
    ttsManager = TextToSpeechManager(this)
    sttManager = SpeechToTextManager(this)

    // Inyectamos en ViewModel
    viewModel.ttsEngine = ttsManager
    viewModel.sttEngine = sttManager

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupUi()
    observeState()
    binding.statusText.text = "Pantalla Cargada!"
  }

  override fun onDestroy() {
    super.onDestroy()
    if (::ttsManager.isInitialized) ttsManager.shutdown()
    if (::sttManager.isInitialized) sttManager.shutdown()
  }

  private fun setupUi() {
    binding.resultsRecycler.layoutManager = LinearLayoutManager(this)
    binding.resultsRecycler.adapter = adapter

    // Se asume que existe loading en el layout
    binding.loading.visibility = View.GONE

    binding.sendButton.setOnClickListener { sendQuery() }
    binding.speakButton.setOnClickListener { checkAudioPermissionAndListen() }

    binding.queryEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEND) {
        sendQuery()
        true
      } else {
        false
      }
    }

    adapter.setOnItemClickListener { item ->
      val intent = Intent(this, EnrollActivity::class.java)
      intent.putExtra(EnrollActivity.EXTRA_ACTIVITY_TITLE, item.title)
      startActivity(intent)
    }

    binding.profileImage.setOnClickListener {
      Toast.makeText(this, "Perfil (pendiente)", Toast.LENGTH_SHORT).show()
    }
  }

  private fun checkAudioPermissionAndListen() {
    when {
      ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
        PackageManager.PERMISSION_GRANTED -> {
        viewModel.onMicClicked()
      }
      else -> requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
  }

  private fun observeState() {
    lifecycleScope.launch {
      viewModel.uiModel.collect { model ->
        binding.statusText.text = model.status
        adapter.submitList(model.items)
      }
    }
  }

  private fun sendQuery() {
    val text = binding.queryEditText.text?.toString().orEmpty()

    if (text.isBlank()) return

    viewModel.onSendQuery(text)

    binding.queryEditText.text?.clear()
    binding.queryEditText.clearFocus()
  }
}
