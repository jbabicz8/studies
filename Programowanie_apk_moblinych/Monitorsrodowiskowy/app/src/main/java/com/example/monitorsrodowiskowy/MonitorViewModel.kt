package com.example.monitorsrodowiskowy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class MonitorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MonitorUiState())
    val uiState = _uiState.asStateFlow()

    fun updateSensors(noise: Double, lat: Double, lng: Double) {
        _uiState.value = _uiState.value.copy(currentNoise = noise, currentLat = lat, currentLng = lng)
    }

    fun saveCurrentMeasurement() {
        val newMeasurement = Measurement(
            noiseLevel = _uiState.value.currentNoise,
            location = "${_uiState.value.currentLat}, ${_uiState.value.currentLng}",
            timestamp = Date().toString()
        )
        _uiState.value = _uiState.value.copy(
            history = _uiState.value.history + newMeasurement
        )
    }

    fun clearHistory() {
        _uiState.value = _uiState.value.copy(history = emptyList())
    }
}