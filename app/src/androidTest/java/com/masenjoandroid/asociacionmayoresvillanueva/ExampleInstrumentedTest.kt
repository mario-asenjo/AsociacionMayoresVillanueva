package com.masenjoandroid.asociacionmayoresvillanueva

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.masenjoandroid.asociacionmayoresvillanueva.ui.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

  @get:Rule
  val rule = ActivityScenarioRule(MainActivity::class.java)

  @Test
  fun mainActivity_launches() {
    // Si llega aquí sin crashear, el test pasa.
  }
}
