package com.example.babytalkmonitor.record

import java.io.File

interface AudioRecorder {
    fun start(outputfile: File)
    fun stop()
}