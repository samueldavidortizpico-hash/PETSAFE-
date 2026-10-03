package com.so.petsafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.so.petsafe.navigation.NavegacionApp
import com.so.petsafe.ui.theme.PetsafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetsafeTheme {
                NavegacionApp()
            }
        }
    }
}