package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.masenjoandroid.asociacionmayoresvillanueva.app.R
import com.masenjoandroid.asociacionmayoresvillanueva.data.repository.UsersRepositoryImpl
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

  private lateinit var usersRepository: UsersRepositoryImpl

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    usersRepository = UsersRepositoryImpl(this)

    val emailInput = findViewById<EditText>(R.id.emailEditText)
    val passwordInput = findViewById<EditText>(R.id.passwordEditText)
    val loginButton = findViewById<Button>(R.id.loginButton)
    val errorText = findViewById<TextView>(R.id.errorTextView)
    val goToRegister = findViewById<TextView>(R.id.goToRegisterTextView)

    loginButton.setOnClickListener {
      val email = emailInput.text.toString()
      val password = passwordInput.text.toString()

      errorText.text = ""

      lifecycleScope.launch {
        val result = usersRepository.login(email, password)

        result.onSuccess {
          // TODO: Navegar a pantalla principal
          // startActivity(Intent(this@LoginActivity, MainActivity::class.java))
          // finish()
        }

        result.onFailure {
          errorText.text = it.message
        }
      }
    }

    goToRegister.setOnClickListener {
      startActivity(Intent(this, RegisterActivity::class.java))
    }
  }
}
