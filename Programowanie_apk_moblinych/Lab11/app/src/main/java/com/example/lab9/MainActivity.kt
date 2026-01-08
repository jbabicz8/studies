package com.example.lab9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lab9.navigation.MovieNavigation
import com.example.lab9.ui.theme.Kurs2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kurs2Theme {
                MovieNavigation()
            }
        }
    }
}
