package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ActivityCompleteBinding
import com.masenjoandroid.asociacionmayoresvillanueva.data.local.AppDatabase
import com.masenjoandroid.asociacionmayoresvillanueva.data.local.CompletionRequestEntity
import com.masenjoandroid.asociacionmayoresvillanueva.voice.SpeechToTextManager
import com.masenjoandroid.asociacionmayoresvillanueva.voice.TextToSpeechManager
import java.util.UUID
import kotlinx.coroutines.launch

class CompleteActivityActivity : AppCompatActivity() {

  companion object {
    const val EXTRA_ACTIVITY_ID = "extra_activity_id"
    const val EXTRA_ACTIVITY_TITLE = "extra_activity_title"
    const val EXTRA_USER_ID = "extra_user_id"
  }

  private lateinit var binding: ActivityCompleteBinding
  private lateinit var tts: TextToSpeechManager
  private lateinit var stt: SpeechToTextManager

  private val userId by lazy { intent.getStringExtra(EXTRA_USER_ID) ?: "uTxjIlmxtBosRv07mWAH" }
  private val activityId by lazy { intent.getStringExtra(EXTRA_ACTIVITY_ID).orEmpty() }
  private val activityTitle by lazy { intent.getStringExtra(EXTRA_ACTIVITY_TITLE).orEmpty() }

  private var rating: Int? = null
  private var difficultyText: String = ""
  private var mediaUri: Uri? = null

  private val pickMedia =
    registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      mediaUri = uri
      binding.mediaValue.text = uri?.toString() ?: "Sin archivo"
      askConfirm()
    }

  private val pickFallback =
    registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
      mediaUri = uri
      binding.mediaValue.text = uri?.toString() ?: "Sin archivo"
      askConfirm()
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityCompleteBinding.inflate(layoutInflater)
    setContentView(binding.root)

    tts = TextToSpeechManager(this)
    stt = SpeechToTextManager(this)

    binding.activityTitle.text = activityTitle

    binding.startVoiceButton.setOnClickListener { startWizard() }
    binding.confirmButton.setOnClickListener { submitPending() }
  }

  override fun onResume() {
    super.onResume()
    startWizard()
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

  private fun startWizard() {
    askRating()
  }

  private fun askRating() {
    binding.status.text = "Paso 1/3: Valoración"
    tts.speak("¿Cómo te has sentido realizando la actividad? Del uno al cinco.") {
      listen { spoken ->
        val parsed = VoiceParser.parseRating(spoken)
        if (parsed == null) {
          tts.speak("No te he entendido. Di un número del uno al cinco.") { askRating() }
        } else {
          rating = parsed
          binding.ratingValue.text = parsed.toString()
          askDifficulty()
        }
      }
    }
  }

  private fun askDifficulty() {
    binding.status.text = "Paso 2/3: Dificultad"
    tts.speak("¿Qué parte de la actividad te ha costado? Dilo con tus palabras.") {
      listen { spoken ->
        difficultyText = spoken.trim()
        binding.difficultyValue.text =
          if (difficultyText.isBlank()) "Sin comentario" else difficultyText
        askMedia()
      }
    }
  }

  private fun askMedia() {
    binding.status.text = "Paso 3/3: Multimedia"
    tts.speak(
      "¿Quieres añadir una foto o vídeo para que tu monitor revise? Voy a abrir la galería."
    ) {
      openPicker()
    }
  }

  private fun openPicker() {
    // Photo Picker moderno
    try {
      pickMedia.launch(
        PickVisualMediaRequest(
          ActivityResultContracts.PickVisualMedia.ImageAndVideo
        )
      )
    } catch (_: Throwable) {
      // Fallback universal
      pickFallback.launch("*/*")
    }
  }

  private fun askConfirm() {
    binding.status.text = "Confirmación"
    val r = rating ?: 0
    val mediaTxt = if (mediaUri != null) "con archivo adjunto" else "sin archivo adjunto"

    tts.speak(
      "Resumen. Valoración $r sobre 5. Comentario: $difficultyText. $mediaTxt. Di confirmar para enviar o cancelar."
    ) {
      listen { spoken ->
        when (VoiceParser.parseConfirm(spoken)) {
          true -> submitPending()
          false -> finish()
          null -> tts.speak("No te he entendido. Di confirmar o cancelar.") { askConfirm() }
        }
      }
    }
  }

  private fun listen(onResult: (String) -> Unit) {
    tts.stop()
    stt.startListening(
      onResult = { onResult(it) },
      onError = { err -> tts.speak(err) { /* reintento suave */ } }
    )
  }

  private fun submitPending() {
    val r = rating ?: 0
    if (r !in 1..5) {
      tts.speak("Falta la valoración del uno al cinco.") { askRating() }
      return
    }
    if (activityId.isBlank()) {
      tts.speak("Falta el identificador de la actividad. No puedo enviar la solicitud.")
      return
    }

    val dao = AppDatabase.get(this).completionRequestDao()
    val entity = CompletionRequestEntity(
      id = UUID.randomUUID().toString(),
      userId = userId,
      activityId = activityId,
      rating = r,
      difficultyText = difficultyText,
      mediaUri = mediaUri?.toString(),
      status = "PENDING",
      createdAt = System.currentTimeMillis()
    )

    lifecycleScope.launch {
      dao.insert(entity)
      binding.status.text = "Enviado (pendiente de revisión)"
      tts.speak("Solicitud enviada. Queda pendiente de revisión por el monitor.") { finish() }
    }
  }
}
