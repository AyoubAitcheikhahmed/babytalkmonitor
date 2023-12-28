package com.example.babytalkmonitor.network

import com.example.babytalkmonitor.model.TranscriptionResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Part

interface BabytalkMonitorService {
    @Multipart
    @POST("upload")
    fun uploadAudio(@Part audioFile: MultipartBody.Part): Call<TranscriptionResponse>
}