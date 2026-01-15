package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ActivityMonitorBinding
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.FeedbackItem
import com.masenjoandroid.asociacionmayoresvillanueva.ui.adapters.MonitorAdapter

class MonitorDashboardActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMonitorBinding

  // Lista de datos MOCK (Simulada)
  private var mockFeedbacks = listOf(
    FeedbackItem(
      id = "1",
      userName = "María López",
      activityTitle = "Gimnasia Suave",
      feelingRating = 5,
      difficultyMessage = "Ninguna, me ha encantado.",
      attachmentUri = null
    ),
    FeedbackItem(
      id = "2",
      userName = "Antonio Ruiz",
      activityTitle = "Taller de Memoria",
      feelingRating = 2,
      difficultyMessage = "Me costó recordar la segunda parte del ejercicio.",
      attachmentUri = "fake_uri",
      isVideo = false
    ),
    FeedbackItem(
      id = "3",
      userName = "Felipe Ramos",
      activityTitle = "Yoga en silla",
      feelingRating = 5,
      difficultyMessage = "La espalda me tiraba un poco pero bien.",
      attachmentUri = "fake_video_uri",
      isVideo = true
    )
  )

  private val adapter = MonitorAdapter { item ->
    markAsReviewed(item)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMonitorBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupRecycler()
    loadData()
  }

  private fun setupRecycler() {
    binding.feedbackRecycler.layoutManager = LinearLayoutManager(this)
    binding.feedbackRecycler.adapter = adapter
  }

  private fun loadData() {
    adapter.submitList(mockFeedbacks)
  }

  private fun markAsReviewed(item: FeedbackItem) {
    if (!item.isReviewed) {
      Toast.makeText(this, "Revisado: ${item.userName}", Toast.LENGTH_SHORT).show()
      // Actualizamos la lista local simulando una BBDD
      mockFeedbacks = mockFeedbacks.map {
        if (it.id == item.id) it.copy(isReviewed = true) else it
      }
      adapter.submitList(mockFeedbacks)
    }
  }
}
