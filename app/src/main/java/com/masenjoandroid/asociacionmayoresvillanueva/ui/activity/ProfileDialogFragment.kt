package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.masenjoandroid.asociacionmayoresvillanueva.app.R

class ProfileDialogFragment : DialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflamos el layout que hemos modificado con el botón nuevo
    return inflater.inflate(R.layout.dialog_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Listener para el botón de Monitor
    view.findViewById<View>(R.id.monitorAccessButton).setOnClickListener {
      // Navegar a la Activity del Monitor
      val intent = Intent(requireContext(), MonitorDashboardActivity::class.java)
      startActivity(intent)
      dismiss() // Cerrar el modal
    }
  }

  override fun onStart() {
    super.onStart()
    // Configuración visual del Dialog (Arriba izquierda)
    dialog?.window?.apply {
      setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
      )
      setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setGravity(Gravity.TOP or Gravity.START)

      val params = attributes
      params.x = 20
      params.y = 20
      attributes = params
    }
  }
}
