package com.example.pontoverde.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PontoVerdeColors = lightColorScheme(
    primary = Verde,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeEscuro,
    background = Fundo,
    surface = Fundo,
    onBackground = TextoPrincipal,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
)

@Composable
fun PontoverdeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PontoVerdeColors,
        typography = Typography,
        content = content
    )
}