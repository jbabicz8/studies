package com.example.monitorsrodowiskowy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val monitorViewModel: MonitorViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val audioAnalyzer = AudioAnalyzer()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.RECORD_AUDIO] == true) {
            audioAnalyzer.start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        ))

        startRealSensorAcquisition()

        setContent {
            MainNavigation(monitorViewModel)
        }
    }

    private fun startRealSensorAcquisition() {
        lifecycleScope.launch {
            try { audioAnalyzer.start() } catch (e: Exception) { e.printStackTrace() }

            while (true) {
                // Pobieranie Hałasu
                val realNoise = if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    audioAnalyzer.getAmplitudeDb()
                } else 0.0

                // Pobieranie GPS
                if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // getCurrentLocation wymusza odczyt teraz, zamiast brać starą wartość
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                        .addOnSuccessListener { location ->
                            location?.let {
                                monitorViewModel.updateSensors(realNoise, it.latitude, it.longitude)
                            } ?: run {
                                monitorViewModel.updateSensors(realNoise, monitorViewModel.uiState.value.currentLat, monitorViewModel.uiState.value.currentLng)
                            }
                        }
                } else {
                    monitorViewModel.updateSensors(realNoise, 0.0, 0.0)
                }

                delay(1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioAnalyzer.stop()
    }
}

@Composable
fun MainNavigation(viewModel: MonitorViewModel = viewModel()) {
    val navController = rememberNavController()
    val state by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = DashboardRoute) {
        composable<DashboardRoute> {
            DashboardScreen(
                state = state,
                onSave = { viewModel.saveCurrentMeasurement() },
                onGoToHistory = { navController.navigate(HistoryRoute) }
            )
        }
        composable<HistoryRoute> {
            HistoryScreen(
                state = state,
                onClear = { viewModel.clearHistory() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}