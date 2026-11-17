package com.example.pontoverde

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.pontoverde.ui.navigation.PontoVerdeNavHost
import com.example.pontoverde.ui.theme.PontoverdeTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuração do osmdroid: cache do mapa e identificação da app perante os servidores
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = "PontoVerde/1.0 (projeto académico, Android)"

        enableEdgeToEdge()
        setContent {
            PontoverdeTheme {
                PontoVerdeNavHost(Modifier.fillMaxSize())
            }
        }
    }
}