package com.example.babytalkmonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.babytalkmonitor.ui.screens.recordingScreen.RecordingScreen
import com.example.babytalkmonitor.ui.theme.BabytalkmonitorTheme
import com.example.babytalkmonitor.ui.theme.BlueBegin
import com.example.babytalkmonitor.ui.theme.BlueEnd

@Composable
fun CardItem(
    title: String,
    cardContent: String,
    contentDescription: String = "",
    beginColor: Color,
    endColor: Color,
    size : Int = 150
) {
    val grad = listOf(
        Color(0xFF6DD5FA), Color(0xFFFF758C), // Sky blue to pinkish
    )

    fun getGradientColor(
        start: Color,
        end: Color
    ): Brush {
        return Brush.horizontalGradient(colors = listOf(start,end))
    }

    Surface(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(size.dp),
        shape = RoundedCornerShape(16.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(getGradientColor(BlueBegin, BlueEnd)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp)
            )
            Text(
                text = cardContent,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.padding(start = 15.dp,bottom = 10.dp)
            )
            if (contentDescription != null || contentDescription != ""){
                Text(
                    text = contentDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    modifier = Modifier.padding(start = 25.dp,bottom = 10.dp))
            }
            }
    }
}
@Composable
@Preview
fun CardItemPreview(){

    CardItem("Verkleinwoorden","schatje, patatje, lievertje, ke,je ...","", BlueBegin, BlueEnd,80)

}

fun createGradientBrush(
    colors: List<Color>,
    isVertical: Boolean = true
): Brush {

    val endOffset = if (isVertical) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp
    )
}