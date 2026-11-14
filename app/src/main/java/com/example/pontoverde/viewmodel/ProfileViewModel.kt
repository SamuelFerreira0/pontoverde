package com.example.pontoverde.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.WineBar
import androidx.lifecycle.ViewModel
import com.example.pontoverde.ui.profile.BinStat
import com.example.pontoverde.ui.profile.RecyclingActivity
import com.example.pontoverde.ui.theme.EcoColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Tudo o que o ecrã de perfil mostra, num único objeto.
data class ProfileUiState(
    val userName: String = "Ana Martins",
    val email: String = "jgmeirafaria@gmail.com",
    val stats: List<BinStat> = sampleStats,
    val activities: List<RecyclingActivity> = sampleActivities,
    val showEmpty: Boolean = false,
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Ação do ecrã: alterna entre a lista e o estado vazio.
    fun toggleEmpty() {
        _uiState.update { it.copy(showEmpty = !it.showEmpty) }
    }
}

// Dados fictícios por agora; mais tarde vêm do Room.
private val sampleStats = listOf(
    BinStat("Amarelo", 12, EcoColors.Yellow),
    BinStat("Azul", 7, EcoColors.Blue),
    BinStat("Vidro", 4, EcoColors.GreenLight),
    BinStat("Pilhão", 1, EcoColors.Red),
)

private val sampleActivities = listOf(
    RecyclingActivity(
        title = "Garrafa de plástico",
        bin = "Ecoponto amarelo",
        time = "Hoje, 09:42",
        icon = Icons.Outlined.LocalDrink,
        iconTint = EcoColors.YellowDark,
        iconContainer = EcoColors.YellowContainer,
    ),
    RecyclingActivity(
        title = "Caixa de cereais",
        bin = "Ecoponto azul",
        time = "Ontem, 18:15",
        icon = Icons.Outlined.Description,
        iconTint = EcoColors.BlueDark,
        iconContainer = EcoColors.BlueContainer,
    ),
    RecyclingActivity(
        title = "Frasco de vidro",
        bin = "Ecoponto verde",
        time = "12 jun., 13:08",
        icon = Icons.Outlined.WineBar,
        iconTint = EcoColors.Green,
        iconContainer = EcoColors.GreenContainer,
    ),
    RecyclingActivity(
        title = "Pilha AA",
        bin = "Pilhão",
        time = "9 jun., 17:30",
        icon = Icons.Filled.BatteryFull,
        iconTint = EcoColors.RedDark,
        iconContainer = EcoColors.RedContainer,
    ),
)