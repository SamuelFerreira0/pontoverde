package com.example.pontoverde.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Ecrã provisório. Substituir pelo ecrã verdadeiro quando o colega o juntar à develop. */
@Composable
fun PlaceholderEcra(nome: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("$nome (em construção)")
    }
}