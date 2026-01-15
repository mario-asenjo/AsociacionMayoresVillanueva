package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.masenjoandroid.asociacionmayoresvillanueva.app.R
import com.masenjoandroid.asociacionmayoresvillanueva.data.repository.UsersRepositoryImpl
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

  private lateinit var usersRepository: UsersRepositoryImpl

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    usersRepository = UsersRepositoryImpl(this)

    val emailInput = findViewById<EditText>(R.id.emailEditText)
    val passwordInput = findViewById<EditText>(R.id.passwordEditText)
    val roleGroup = findViewById<RadioGroup>(R.id.roleRadioGroup)
    val registerButton = findViewById<Button>(R.id.registerButton)
    val errorText = findViewById<TextView>(R.id.errorTextView)

    registerButton.setOnClickListener {
      val email = emailInput.text.toString()
      val password = passwordInput.text.toString()

      val selectedRoleId = roleGroup.checkedRadioButtonId
      val rol = when (selectedRoleId) {
        R.id.radioMonitor -> "monitor"
        else -> "usuario"
      }

      errorText.text = ""

      lifecycleScope.launch {
        val result = usersRepository.registerUser(
          email = email,
          password = password,
          rol = rol,
          restricciones = emptyList()
        )

        result.onSuccess {
          finish() // Volver al login
        }

        result.onFailure {
          errorText.text = it.message
        }
      }
    }
  }
}
