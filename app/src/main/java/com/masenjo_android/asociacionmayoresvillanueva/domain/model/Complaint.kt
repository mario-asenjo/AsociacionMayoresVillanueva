package com.masenjo_android.asociacionmayoresvillanueva.domain.model

data class Complaint(
    val id: String,
    val title: String,
    val body: String,
    val createdAt: String,
    val authorId: String,
)
