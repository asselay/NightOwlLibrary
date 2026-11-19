package com.nightowl.library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nightowl.library.ui.NightOwlApp
import com.nightowl.library.ui.theme.NightOwlLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NightOwlLibraryTheme {
                NightOwlApp()
            }
        }
    }
}