package com.masenjo_android.asociacionmayoresvillanueva.domain.model

data class UserProfile(
    val id: String,
    val displayName: String,
    val restrictions: List<String>,
    val xp: Int,
)
