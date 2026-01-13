package com.masenjo_android.asociacionmayoresvillanueva.common

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Content<T>(val data: T) : UiState<T>()
    data class Message(val text: String) : UiState<Nothing>()
    data class Error(val text: String) : UiState<Nothing>()
}
