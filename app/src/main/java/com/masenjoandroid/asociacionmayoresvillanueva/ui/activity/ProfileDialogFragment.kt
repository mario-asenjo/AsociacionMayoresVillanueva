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

// IMPORTANTE: Asegúrate de que este import sea el correcto.
// Si te sale en rojo, bórralo y deja que Android Studio te sugiera importar "tu.paquete.R"


class ProfileDialogFragment : DialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Si la app peta aquí, suele ser porque no encuentra R.layout.dialog_profile
    return inflater.inflate(R.layout.dialog_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Configuramos el botón del monitor
    // Usamos un try-catch por seguridad para evitar que cierre la app si el botón falla
    try {
      view.findViewById<View>(R.id.monitorAccessButton)?.setOnClickListener {
        val intent = Intent(requireContext(), MonitorDashboardActivity::class.java)
        startActivity(intent)
        dismiss()
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun onStart() {
    super.onStart()
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
