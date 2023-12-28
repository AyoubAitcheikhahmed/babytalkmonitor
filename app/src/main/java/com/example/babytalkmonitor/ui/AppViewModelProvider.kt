package com.example.babytalkmonitor.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    private var Instance: AppViewModel? = null

    val Factory = viewModelFactory {
        initializer {
            if (Instance == null) {
                Instance = AppViewModel()
            }
            Instance!!

        }
    }

    val Reset = {
        Instance = null
    }
}

