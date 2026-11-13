package com.example.pontoverde.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

private class ItemBarra(val rota: String, val nome: String, val icone: ImageVector)

private val itens = listOf(
    ItemBarra(Rotas.CAMARA, "Câmara", Icons.Outlined.PhotoCamera),
    ItemBarra(Rotas.MAPA, "Mapa", Icons.Outlined.Map),
    ItemBarra(Rotas.PERFIL, "Perfil", Icons.Outlined.Person),
    ItemBarra(Rotas.REPORTAR, "Reportar", Icons.Outlined.Campaign)
)

@Composable
fun BarraInferior(rotaAtual: String?, onNavegar: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        itens.forEach { item ->
            NavigationBarItem(
                selected = rotaAtual == item.rota,
                onClick = { onNavegar(item.rota) },
                icon = { Icon(item.icone, contentDescription = item.nome) },
                label = { Text(item.nome) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1B5E20),
                    selectedTextColor = Color(0xFF1B5E20),
                    indicatorColor = Color(0xFFE2F2E4)
                )
            )
        }
    }
}