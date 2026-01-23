package com.example.monitorsrodowiskowy

import kotlinx.serialization.Serializable

data class Measurement(
    val id: Long = System.currentTimeMillis(),
    val timestamp: String,
    val noiseLevel: Double,
    val location: String
)

data class MonitorUiState(
    val currentNoise: Double = 0.0,
    val currentLat: Double = 0.0,
    val currentLng: Double = 0.0,
    val history: List<Measurement> = emptyList()
)

@Serializable object DashboardRoute
@Serializable object HistoryRoute