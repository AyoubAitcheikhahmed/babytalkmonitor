package com.example.babytalkmonitor.ui.screens.loadingScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.babytalkmonitor.R
import com.example.babytalkmonitor.ui.theme.BabytalkmonitorTheme
import kotlinx.coroutines.delay


@Composable
fun LoadingScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    proceedToNextScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(4300)
        // Simulate a 2-second loading delay
        proceedToNextScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.hogentlogo),
            contentDescription = "hogent logo",
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text =  "Welkom ...",
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic
        )
    }
}
@Composable
@Preview
fun LoadingScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        BabytalkmonitorTheme {
            LoadingScreen(proceedToNextScreen = { })
        }
    }
}