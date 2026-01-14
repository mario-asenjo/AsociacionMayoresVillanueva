package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.masenjoandroid.asociacionmayoresvillanueva.app.databinding.ActivityEnrollBinding

class EnrollActivity : AppCompatActivity() {

  companion object {
    const val EXTRA_ACTIVITY_TITLE = "extra_activity_title"
  }

  private lateinit var binding: ActivityEnrollBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityEnrollBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val title = intent.getStringExtra(EXTRA_ACTIVITY_TITLE).orEmpty()

    binding.activityTitle.text = title
    binding.confirmButton.setOnClickListener {
      finish() // Más adelante aquí va Firebase
    }
  }
}
