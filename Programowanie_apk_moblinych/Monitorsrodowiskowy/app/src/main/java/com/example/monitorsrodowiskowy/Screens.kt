@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.monitorsrodowiskowy

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(state: MonitorUiState, onSave: () -> Unit, onGoToHistory: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MONITOR ŚRODOWISKA", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoCard(
                label = "Poziom Hałasu (Mikrofon)",
                value = "${String.format("%.1f", state.currentNoise)} dB",
                color = if (state.currentNoise > 80) Color.Red else MaterialTheme.colorScheme.secondary
            )

            InfoCard(
                label = "Lokalizacja (GPS)",
                value = "Lat: ${String.format("%.4f", state.currentLat)}\nLng: ${String.format("%.4f", state.currentLng)}",
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ZAPISZ POMIAR", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onGoToHistory,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("HISTORIA I EKSPORT")
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun HistoryScreen(state: MonitorUiState, onClear: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista pomiarów") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("←") }
                },
                actions = {
                    TextButton(onClick = onClear) {
                        Text("WYCZYŚĆ", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val report = state.history.joinToString("\n") {
                        "${it.timestamp}: ${it.noiseLevel} dB, Poz: ${it.location}"
                    }
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Raport Środowiskowy")
                        putExtra(Intent.EXTRA_TEXT, report)
                    }
                    context.startActivity(Intent.createChooser(intent, "Wyślij raport"))
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text("Wyślij", modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    ) { padding ->
        if (state.history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Brak zapisanych pomiarów", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.history) { m ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(m.timestamp, style = MaterialTheme.typography.labelSmall)
                            Text("${m.noiseLevel} dB", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(m.location, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}