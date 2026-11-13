package com.example.pontoverde

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.pontoverde.ui.navigation.PontoVerdeNavHost
import com.example.pontoverde.ui.theme.PontoverdeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PontoverdeTheme {
                PontoVerdeNavHost(Modifier.fillMaxSize())
            }
        }
    }
}