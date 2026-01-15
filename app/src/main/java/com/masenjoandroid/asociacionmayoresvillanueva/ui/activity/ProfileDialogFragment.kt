package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.app.Dialog
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
// Asegúrate de importar R de tu paquete base si falla el import de arriba
// import com.masenjoandroid.asociacionmayoresvillanueva.R

class ProfileDialogFragment : DialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.dialog_profile, container, false)
  }

  override fun onStart() {
    super.onStart()
    // Configuración para que salga arriba a la izquierda
    dialog?.window?.apply {
      setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
      )
      setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

      // Posicionamos el dialog arriba a la izquierda
      setGravity(Gravity.TOP or Gravity.START)

      // Un poco de margen para que no se pegue al borde exacto
      val params = attributes
      params.x = 20 // Margen X
      params.y = 20 // Margen Y
      attributes = params
    }
  }
}
