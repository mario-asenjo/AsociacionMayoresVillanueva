package com.masenjoandroid.asociacionmayoresvillanueva.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ItemActivityBinding
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.ActivityItem

class ActivitiesAdapter(private val onClick: (ActivityItem) -> Unit = {}) :
  ListAdapter<ActivityItem, ActivitiesAdapter.VH>(DIFF) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    val binding = ItemActivityBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return VH(binding, onClick)
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.bind(getItem(position))
  }

  class VH(private val binding: ItemActivityBinding, private val onClick: (ActivityItem) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ActivityItem) {
      binding.title.text = item.title
      binding.subtitle.text = "${item.dateTime} · ${item.placeName}"
      binding.tags.text = item.tags.joinToString(" ") { "#$it" }

      binding.cardRoot.contentDescription =
        "Actividad ${item.title}. ${item.dateTime}. Lugar ${item.placeName}."

      binding.root.setOnClickListener { onClick(item) }
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
