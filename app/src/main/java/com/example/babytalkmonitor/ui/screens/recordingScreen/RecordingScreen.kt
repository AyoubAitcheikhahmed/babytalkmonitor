package com.example.babytalkmonitor.ui.screens.recordingScreen

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.babytalkmonitor.R
import com.example.babytalkmonitor.playback.BabyTalkAudioPlayer
import com.example.babytalkmonitor.record.BabyTalkAudioRecorder
import com.example.babytalkmonitor.ui.AppViewModel
import com.example.babytalkmonitor.ui.AppViewModelProvider
import com.example.babytalkmonitor.ui.theme.BabytalkmonitorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import com.arthenica.mobileffmpeg.FFmpeg
import androidx.compose.runtime.collectAsState
import com.example.babytalkmonitor.network.ApiState
import com.example.babytalkmonitor.ui.components.AppGuide
import com.example.babytalkmonitor.ui.components.CardItem
import com.example.babytalkmonitor.ui.theme.BlueBegin
import com.example.babytalkmonitor.ui.theme.BlueEnd

@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@Composable
fun RecordingScreen(
    innerPadding : PaddingValues = PaddingValues(0.dp),
    proceedToNextScreen: () -> Unit
) {


    val appViewModel: AppViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val context = LocalContext.current
    val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val errorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    val loadingProgress by animateLottieCompositionAsState(composition = loadingComposition, iterations = LottieConstants.IterateForever)
    val errorProgress by animateLottieCompositionAsState(composition = errorComposition, iterations = LottieConstants.IterateForever)

    val transcriptionResponse = appViewModel.transcriptionState.value

    var audioRecordFile: File? = null
    val recorder by lazy {
        BabyTalkAudioRecorder(context)
    }

    var isRecording by remember { mutableStateOf(false) }
    var isResponseWaiting by remember { mutableStateOf(false) }


    val player by lazy {
        BabyTalkAudioPlayer(context)
    }

    var isFileReady by remember { mutableStateOf(false) }
    var showPlayback by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }

    var scriptie = "Bij Projecten DevOps (Development en Operations) leveren jullie een volledig afgewerkt product op in samenwerking met een externe klant. Net als bij de vorige projecten werken we hier op een Agile manier. Zowel het proces als het project worden gevolgd en geëvalueerd. De agile werkwijze (Scrum/Kanban) toepassen, samenwerking met het team en andere teams, samenwerking met de klant, de rapportering en de project opvolging zijn de basis van evaluatie voor het luik analyse en proces."



    LaunchedEffect(key1 = isFileReady) {
        if (isFileReady) {
            showPlayback = true
        }
    }

    LaunchedEffect(isEnabled) {
        if (isEnabled) return@LaunchedEffect
        else delay(1000L)
        isEnabled = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (appViewModel.apiResponseState.value) {
            ApiState.WAITING -> {
                LottieAnimation(
                    modifier = Modifier.size(150.dp),
                    composition = loadingComposition,
                    progress = {loadingProgress}
                )
            }
            ApiState.SUCCESS ->{
                LazyColumn (
                    modifier = Modifier
                        .height(500.dp)
                ) {

                    if (transcriptionResponse != null && transcriptionResponse.success) {
                        item {
                            CardItem(
                                title = "Transcriptie:",
                                cardContent = scriptie ?: "",
                                contentDescription = transcriptionResponse.message ?: "",
                                beginColor = BlueBegin,
                                endColor = BlueEnd,
                                size = 300
                            )
                        }
                    }
                }
            }
            ApiState.ERROR -> {
                LottieAnimation(
                    modifier = Modifier.size(150.dp),
                    composition = errorComposition,
                    progress = {errorProgress}
                )
            }
            ApiState.IDLE -> {
                AppGuide()
            }
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Icon buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                onClick = {
                    Toast.makeText(context, "Opnemen gestart", Toast.LENGTH_SHORT).show()
                    File(context.cacheDir, "speechRecording.mp3").also {
                        recorder.start(it)
                        audioRecordFile = it
                    }
                },
                modifier = Modifier
                    .size(72.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(id =R.drawable.baseline_mic_24),
                    contentDescription = "Start",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = {
                    Toast.makeText(context, "Opnemen gestopt", Toast.LENGTH_SHORT).show()
                    recorder.stop()
                    appViewModel.convertToFlac(
                        context.cacheDir.path + "/speechRecording.mp3",
                        context.cacheDir.path + "/speechRecording_flac.flac"
                    )
                },
                modifier = Modifier
                    .size(72.dp)
                    .background(MaterialTheme.colorScheme.error, CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(id =R.drawable.baseline_stop_24),
                    contentDescription = "Stop",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = {
                    Toast.makeText(context, "Audio aan het spelen...", Toast.LENGTH_SHORT).show()
                    player.playFile(audioRecordFile ?: return@IconButton)
                },
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.Gray, CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(id =R.drawable.baseline_play_arrow_24),
                    contentDescription = "Test Audio",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                onClick = {
                    Toast.makeText(context, "processing", Toast.LENGTH_SHORT).show()
                    isResponseWaiting = true
                    val flacAudio = File(context.cacheDir.path + "/speechRecording_flac.flac")
                        CoroutineScope(Dispatchers.Main).launch {
                            val flacAudio = File(context.cacheDir.path + "/speechRecording_flac.flac")
                            appViewModel.sendAudioToApi(flacAudio){ isSuccess ->
                                isResponseWaiting = false
                            }

                    }
                },
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(android.graphics.Color.parseColor("#4bbaa1")), CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Test Audio",
                    tint = Color.White
                )
            }
        }
    }

}


@Composable
@Preview
fun RecordingScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        BabytalkmonitorTheme {
            RecordingScreen(proceedToNextScreen = { })
        }
    }
}
