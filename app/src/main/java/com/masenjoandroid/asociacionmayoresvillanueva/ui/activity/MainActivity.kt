package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

  private val adapter = ActivitiesAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    ttsManager = TextToSpeechManager(this)
    sttManager = SpeechToTextManager(this)

    viewModel.ttsEngine = ttsManager
    viewModel.sttEngine = sttManager

    setupUi()
    observeState()
  }

  override fun onDestroy() {
    super.onDestroy()
    ttsManager.shutdown()
    sttManager.shutdown()
  }

  private fun setupUi() {
    binding.resultsRecycler.layoutManager = LinearLayoutManager(this)
    binding.resultsRecycler.adapter = adapter

    adapter.setOnItemClickListener { item ->
      val intent = Intent(this, EnrollActivity::class.java)
      intent.putExtra(EnrollActivity.EXTRA_ACTIVITY_TITLE, item.title)
      startActivity(intent)
    }


    binding.profileImage.setOnClickListener {
      Toast.makeText(this, "Perfil (pendiente)", Toast.LENGTH_SHORT).show()
    }

    binding.sendButton.setOnClickListener {
      sendQuery()
    }

    binding.speakButton.setOnClickListener {
      viewModel.onMicClicked()
    }

    binding.queryEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEND) {
        sendQuery()
        true
      } else false
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
    viewModel.onSendQuery(text)
  }
}
