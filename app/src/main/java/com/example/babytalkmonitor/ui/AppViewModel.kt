package com.example.babytalkmonitor.ui

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.babytalkmonitor.model.TranscriptionResponse
import com.example.babytalkmonitor.network.ApiClient
import com.example.babytalkmonitor.network.ApiState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException


class AppViewModel() : ViewModel() {
    var apiResponseState = mutableStateOf(ApiState.IDLE)
        private set
    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    val apiService = ApiClient.apiService

    var transcriptionState = mutableStateOf<TranscriptionResponse?>(null)
        private set

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }

    }

    fun convertToFlac(inputFilePath: String, outputFilePath: String) {
        val ffmpegCommand = arrayOf(
            "-y",
            "-i", inputFilePath,   // Input file (MP3)
            "-c:a", "flac",       // FLAC audio codec
            "-q:a", "0",          // Use best quality
            outputFilePath        // Output file (FLAC)
        )

        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(outputFilePath)
            mediaPlayer.prepare()
            val duration = mediaPlayer.duration // duration in milliseconds
            Log.d("AudioFile", "Duration: $duration ms")
        } catch (e: IOException) {
            Log.e("AudioFile", "Error getting duration", e)
        } finally {
            mediaPlayer.release()
        }
        // Execute FFmpeg command
        FFmpeg.execute(ffmpegCommand)
    }


    fun sendAudioToApi(audioFile: File?,onResult:(Boolean) -> Unit) {


        // Ensure the file is not null
        audioFile ?: return

        val fileSize = audioFile.length()
        Log.d("AudioFile", " AudioFile Size: $fileSize bytes")

        //update api state : inorder to skip errors
        apiResponseState.value = ApiState.WAITING

        // Create a request body with the specific audio file MIME type
        val requestFile = audioFile.asRequestBody("audio/flac".toMediaTypeOrNull())
        val audioPart = MultipartBody.Part.createFormData("file", audioFile.name, requestFile) // Specify part name here

        // Enqueue the call to run asynchronously
        apiService.uploadAudio(audioPart).enqueue(object : Callback<TranscriptionResponse> {
            override fun onResponse(call: Call<TranscriptionResponse>, response: Response<TranscriptionResponse>) {
                // Handle the API response here
                if (response.isSuccessful) {
                    Log.e("RESPONSE SUCCESS ","RESPONSE: >> ${response.body() } ,  ${response.message()}")
                    transcriptionState.value = response.body()
                    apiResponseState.value = if (response.isSuccessful) ApiState.SUCCESS else ApiState.ERROR
                    onResult(true)
                } else {
                    Log.e("RESPONSE ELSE", "Status Code: ${response.code()}, Message: ${response.message()}")
                    apiResponseState.value = ApiState.ERROR
                }
            }

            override fun onFailure(call: Call<TranscriptionResponse>, t: Throwable) {
                Log.e("RESPONSE FAIL ","RESPONSE: >> ${t.message } ")
                apiResponseState.value = ApiState.ERROR

            }
        })
    }

}