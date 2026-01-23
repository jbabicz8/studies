package com.example.monitorsrodowiskowy

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlin.math.log10

class AudioAnalyzer {
    private var audioRecord: AudioRecord? = null
    private val sampleRate = 44100
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
    )

    @SuppressLint("MissingPermission")
    fun start() {
        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
            audioRecord?.startRecording()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAmplitudeDb(): Double {
        val buffer = ShortArray(bufferSize)
        val readSize = audioRecord?.read(buffer, 0, bufferSize) ?: 0
        if (readSize <= 0) return 0.0

        var maxAmplitude = 0.0
        for (i in 0 until readSize) {
            val absValue = Math.abs(buffer[i].toInt()).toDouble()
            if (absValue > maxAmplitude) maxAmplitude = absValue
        }

        // Obliczanie dB
        val db = if (maxAmplitude > 0) 20 * log10(maxAmplitude / 32767.0) + 90 else 0.0
        return db.coerceIn(0.0, 120.0) // Ograniczenie zakresu dla czytelności UI
    }

    fun stop() {
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioRecord = null
    }
}