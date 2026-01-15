package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ActivityEnrollBinding
import com.masenjoandroid.asociacionmayoresvillanueva.data.local.AppDatabase
import com.masenjoandroid.asociacionmayoresvillanueva.data.repository.EnrollmentRepositoryImpl
import com.masenjoandroid.asociacionmayoresvillanueva.domain.repository.EnrollmentRepository
import com.masenjoandroid.asociacionmayoresvillanueva.voice.SpeechToTextManager
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechManager
import kotlinx.coroutines.launch

class EnrollActivity : AppCompatActivity() {

  companion object {
    const val EXTRA_ACTIVITY_ID = "extra_activity_id"
    const val EXTRA_ACTIVITY_TITLE = "extra_activity_title"
    const val EXTRA_USER_ID = "extra_user_id"
  }

  private var isFinishingFlow = false
  private lateinit var binding: ActivityEnrollBinding
  private lateinit var tts: TextToSpeechManager
  private lateinit var stt: SpeechToTextManager
  private lateinit var enrollmentRepo: EnrollmentRepository

  private val userId: String by lazy {
    intent.getStringExtra(EXTRA_USER_ID)
      ?: "uTxjIlmxtBosRv07mWAH"
  }
  private val activityId: String by lazy { intent.getStringExtra(EXTRA_ACTIVITY_ID).orEmpty() }
  private val activityTitle: String by lazy {
    intent.getStringExtra(EXTRA_ACTIVITY_TITLE).orEmpty()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityEnrollBinding.inflate(layoutInflater)
    setContentView(binding.root)

    tts = TextToSpeechManager(this)
    stt = SpeechToTextManager(this)

    enrollmentRepo = EnrollmentRepositoryImpl(AppDatabase.get(this).enrollmentDao())

    binding.activityTitle.text = activityTitle

    binding.confirmButton.setOnClickListener {
      confirmEnroll()
    }
  }

  override fun onResume() {
    super.onResume()
    startHandsFree()
  }

  override fun onPause() {
    super.onPause()
    stt.stopListening()
  }

  override fun onDestroy() {
    super.onDestroy()
    stt.shutdown()
    tts.shutdown()
  }

  private fun startHandsFree() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
      PackageManager.PERMISSION_GRANTED
    ) {
      tts.speak(
        "Necesito permiso de micrófono para inscribirte por voz. Puedes pulsar el botón confirmar."
      )
      return
    }

    val title = if (activityTitle.isBlank()) "esta actividad" else activityTitle
    val prompt = "Inscripción a $title. Di confirmar para apuntarte, o cancelar para volver."

    tts.speak(prompt) {
      stt.startListening(
        onResult = { spoken -> handleVoice(spoken) },
        onError = { err -> tts.speak(err) { startHandsFree() } }
      )
    }
  }

  private fun handleVoice(text: String) {
    val n = text.lowercase()
    when {
      n.contains("confirm") || n.contains("sí") || n.contains("si") -> confirmEnroll()
      n.contains("cancel") || n.contains("no") -> {
        stt.stopListening()
        setResult(RESULT_CANCELED)
        finish()
      }
      else -> tts.speak("No te he entendido. Di confirmar o cancelar.") { startHandsFree() }
    }
  }

  private fun confirmEnroll() {
    if (isFinishingFlow) return
    isFinishingFlow = true

    if (activityId.isBlank()) {
      isFinishingFlow = false
      tts.speak("Falta el identificador de la actividad. No puedo inscribirte.")
      return
    }

    // Detenemos escucha para que no re-dispare nada
    stt.stopListening()

    lifecycleScope.launch {
      enrollmentRepo.enroll(userId, activityId)

      // No bloquees el finish esperando callbacks de TTS
      tts.speak("Inscripción confirmada.")

      // Devuelve OK por si quieres refrescar en Main
      setResult(RESULT_OK)
      finish()
    }
  }
}
