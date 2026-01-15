package com.masenjoandroid.asociacionmayoresvillanueva.ui.activity

object VoiceParser {

  fun parseRating(text: String): Int? {
    val t = text.trim().lowercase()
    Regex("""\b([1-5])\b""").find(t)?.groupValues?.get(1)?.toIntOrNull()?.let { return it }
    return when {
      t.contains("uno") -> 1
      t.contains("dos") -> 2
      t.contains("tres") -> 3
      t.contains("cuatro") -> 4
      t.contains("cinco") -> 5
      else -> null
    }
  }

  fun parseConfirm(text: String): Boolean? {
    val t = text.trim().lowercase()
    return when {
      t.contains("confirm") || t.contains("vale") || t.contains("sí") || t.contains("si") -> true
      t.contains("cancel") || t.contains("no") -> false
      else -> null
    }
  }
}
