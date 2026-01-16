package com.masenjoandroid.asociacionmayoresvillanueva.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
// Asegúrate de que este import R coincide con tu paquete base.
// Si te sale en rojo, bórralo y deja que Android Studio importe el correcto (suele ser tu.paquete.R)
import com.masenjoandroid.asociacionmayoresvillanueva.app.R
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ItemActivityBinding
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem

class ActivitiesAdapter : ListAdapter<ActivityItem, ActivitiesAdapter.VH>(DIFF) {

  private var onItemClick: ((ActivityItem) -> Unit)? = null

  fun setOnItemClickListener(listener: (ActivityItem) -> Unit) {
    onItemClick = listener
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    val binding = ItemActivityBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return VH(binding) { item -> onItemClick?.invoke(item) }
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.bind(getItem(position))
  }

  class VH(
    private val binding: ItemActivityBinding,
    private val click: (ActivityItem) -> Unit
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ActivityItem) {
      binding.title.text = item.title
      binding.subtitle.text = "${item.dateTime} · ${item.placeName}"
      binding.tags.text = item.tags.joinToString(" ") { "#$it" }

      // --- LÓGICA DE IMÁGENES ---
      val titleLower = item.title.lowercase()

      val imageRes = when {
        titleLower.contains("paseo") -> R.drawable.img_paseo
        titleLower.contains("gimnasia") -> R.drawable.img_gimnasia
        titleLower.contains("estiramientos") -> R.drawable.img_estiramientos
        // Si no coincide con ninguna, usa el vector que me pasaste
        else -> R.drawable.ic_activity_placeholder
      }

      binding.photo.setImageResource(imageRes)
      // --------------------------

      binding.root.setOnClickListener { click(item) }
    }
  }

  companion object {
    private val DIFF = object : DiffUtil.ItemCallback<ActivityItem>() {
      override fun areItemsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean =
        oldItem == newItem
    }
  }
}
