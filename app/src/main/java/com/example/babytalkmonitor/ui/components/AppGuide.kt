package com.example.babytalkmonitor.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppGuide() {
    // Predefined list of tips
    val tips = listOf(
        TipItem("Play", "Druk op play icon om te beginnen met opnemen" ),
        TipItem("Stop", "Stop om te stoppen"),
        TipItem("Transcribe", "Als einde druk je op stuur voor uw transcriptie")
        // Add more predefined tips as needed
    )

    // Predefined colors
    val beginColor = Color(0xFF6DD5FA) // Example: Sky blue
    val endColor = Color(0xFFFF758C)   // Example: Pinkish

    LazyColumn {
        items(tips) { tip ->
            CardItem(
                title = tip.title,
                cardContent = tip.content,
                beginColor = beginColor,
                endColor = endColor,
                size = 80
            )
        }
    }
}

data class TipItem(
    val title: String,
    val content: String,
)
