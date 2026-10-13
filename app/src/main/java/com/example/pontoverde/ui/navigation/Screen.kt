package com.example.pontoverde.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Report
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Camara : Screen("camara", "Câmara", Icons.Filled.CameraAlt)
    data object Historico : Screen("historico", "Histórico", Icons.Filled.History)
    data object Mapa : Screen("mapa", "Mapa", Icons.Filled.Map)
    data object Reportar : Screen("reportar", "Reportar", Icons.Filled.Report)

    companion object {
        val all = listOf(Camara, Historico, Mapa, Reportar)
    }
}
