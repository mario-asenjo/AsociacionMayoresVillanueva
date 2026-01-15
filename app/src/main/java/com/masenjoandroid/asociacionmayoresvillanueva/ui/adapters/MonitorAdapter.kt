package com.masenjoandroid.asociacionmayoresvillanueva.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ItemFeedbackBinding
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.FeedbackItem

class MonitorAdapter(private val onReviewClick: (FeedbackItem) -> Unit) :
  ListAdapter<FeedbackItem, MonitorAdapter.FeedbackViewHolder>(DiffCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
    val binding = ItemFeedbackBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return FeedbackViewHolder(binding, onReviewClick)
  }

  override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  class FeedbackViewHolder(
    private val binding: ItemFeedbackBinding,
    private val onReviewClick: (FeedbackItem) -> Unit
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedbackItem) {
      // Cabecera
      binding.studentName.text = item.userName
      binding.activityName.text = item.activityTitle

      // 1. Sentimiento
      binding.ratingText.text = when (item.feelingRating) {
        1 -> "1/5 (Muy mal 😞)"
        2 -> "2/5 (Mal 😕)"
        3 -> "3/5 (Regular 😐)"
        4 -> "4/5 (Bien 🙂)"
        5 -> "5/5 (Muy bien 😄)"
        else -> "${item.feelingRating}/5"
      }

      // 2. Dificultad
      binding.difficultyText.text = if (item.difficultyMessage.isNotBlank()) {
        item.difficultyMessage
      } else {
        "Sin comentarios."
      }

      // 3. Adjuntos
      if (item.attachmentUri.isNullOrEmpty()) {
        // No hay foto
        binding.attachmentImage.visibility = View.GONE
        binding.playIcon.visibility = View.GONE
        binding.noAttachmentText.visibility = View.VISIBLE
      } else {
        // Hay foto/video (En un caso real usaríamos Glide o Coil para cargar la URL)
        binding.attachmentImage.visibility = View.VISIBLE
        binding.noAttachmentText.visibility = View.GONE

        // Si es video mostramos el icono de play
        binding.playIcon.visibility = if (item.isVideo) View.VISIBLE else View.GONE

        // Placeholder visual porque no tenemos librería de imágenes aún
        // binding.attachmentImage.setImageURI(Uri.parse(item.attachmentUri))
      }

      // Estado Revisado
      if (item.isReviewed) {
        binding.root.alpha = 0.5f
        binding.reviewCheck.setIconResource(android.R.drawable.checkbox_on_background)
      } else {
        binding.root.alpha = 1.0f
        binding.reviewCheck.setIconResource(android.R.drawable.checkbox_off_background)
      }

      binding.reviewCheck.setOnClickListener { onReviewClick(item) }
    }
  }

  companion object DiffCallback : DiffUtil.ItemCallback<FeedbackItem>() {
    override fun areItemsTheSame(oldItem: FeedbackItem, newItem: FeedbackItem) =
      oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: FeedbackItem, newItem: FeedbackItem) =
      oldItem == newItem
  }
}
