package com.example.babytalkmonitor.model

data class TranscriptionResponse(
    val success: Boolean,
    val message: String?,
    val transcription: String?,
)
