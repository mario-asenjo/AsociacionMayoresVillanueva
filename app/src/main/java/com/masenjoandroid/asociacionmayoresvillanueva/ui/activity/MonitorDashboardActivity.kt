import androidx.appcompat.app.AppCompatActivity
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ActivityMonitorBinding
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.FeedbackItem

// ... imports

class MonitorDashboardActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMonitorBinding

  // Lista de datos MOCK ACTUALIZADA
  private var mockFeedbacks = listOf(
    FeedbackItem(
      id = "1",
      userName = "María López",
      activityTitle = "Gimnasia Suave",
      feelingRating = 5,
      difficultyMessage = "Ninguna, me ha encantado.",
      attachmentUri = null // Sin foto
    ),
    FeedbackItem(
      id = "2",
      userName = "Antonio Ruiz",
      activityTitle = "Taller de Memoria",
      feelingRating = 2,
      difficultyMessage = "Me costó recordar la segunda parte del ejercicio.",
      attachmentUri = "fake_uri", // Simula que hay foto
      isVideo = false
    ),
    FeedbackItem(
      id = "3",
      userName = "Felipe Ramos",
      activityTitle = "Yoga en silla",
      feelingRating = 5,
      difficultyMessage = "La espalda me tiraba un poco pero bien.",
      attachmentUri = "fake_video_uri", // Simula video
      isVideo = true
    )
  )
}

// ... (El resto del código de la activity se mantiene igual, usando el adapter actualizado)
