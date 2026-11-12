package com.example.pontoverde.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// Bottom navigation
// ---------------------------------------------------------------------------

enum class EcoTab(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    Camera("Câmara", Icons.Outlined.PhotoCamera, Icons.Filled.PhotoCamera),
    Map("Mapa", Icons.Outlined.Map, Icons.Filled.Map),
    Profile("Perfil", Icons.Outlined.Person, Icons.Filled.Person),
    Report("Reportar", Icons.Outlined.Campaign, Icons.Filled.Campaign),
}

@Composable
fun EcoBottomBar(
    selected: EcoTab,
    onSelect: (EcoTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shadowElevation = 12.dp,
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
        ) {
            EcoTab.entries.forEach { tab ->
                val isSelected = tab == selected
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onSelect(tab) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.icon,
                            contentDescription = tab.label,
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EcoColors.Green,
                        selectedTextColor = EcoColors.Green,
                        indicatorColor = EcoColors.GreenContainer,
                        unselectedIconColor = EcoColors.TextSecondary,
                        unselectedTextColor = EcoColors.TextSecondary,
                    ),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Small building blocks
// ---------------------------------------------------------------------------

/** Tiny green, letter-spaced caption ("A TUA CONTA", "AJUDA A TUA COMUNIDADE"...). */
@Composable
fun Overline(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = EcoColors.Green,
    fontSize: TextUnit = 11.sp,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
    )
}

/** Rounded square with a leaf, shown in the top-right corner of the headers. */
@Composable
fun LeafBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(EcoColors.GreenContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.Eco,
            contentDescription = null,
            tint = EcoColors.Green,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
fun ScreenHeader(
    overline: String,
    title: String,
    modifier: Modifier = Modifier,
    titleSize: TextUnit = 28.sp,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Overline(overline)
            Text(
                text = title,
                fontSize = titleSize,
                fontWeight = FontWeight.Medium,
                color = EcoColors.TextPrimary,
            )
        }
        LeafBadge()
    }
}

/** White rounded surface with a hairline border and a soft shadow. */
fun Modifier.ecoCard(
    shape: Shape = RoundedCornerShape(20.dp),
    elevation: Dp = 4.dp,
): Modifier = this
    .shadow(
        elevation = elevation,
        shape = shape,
        ambientColor = Color(0x1A000000),
        spotColor = Color(0x1A000000),
    )
    .background(Color.White, shape)
    .border(1.dp, EcoColors.Outline, shape)

object EcoColors {
    // Brand greens
    val Green = Color(0xFF2E7D32)
    val GreenDark = Color(0xFF1B5E20)
    val GreenLight = Color(0xFF81C784)
    val GreenContainer = Color(0xFFDFF0E0)
    val GreenSurface = Color(0xFFEEF7EE)

    // Surfaces & lines
    val Background = Color(0xFFF6FBF6)
    val Surface = Color.White
    val Outline = Color(0xFFDDE6DD)

    // Text
    val TextPrimary = Color(0xFF1B1D1B)
    val TextSecondary = Color(0xFF5B665B)
    val TextMuted = Color(0xFF7C877C)

    // Recycling bins
    val Yellow = Color(0xFFFBC02D)
    val YellowDark = Color(0xFF7A5C00)
    val YellowContainer = Color(0xFFFFF0C2)

    val Blue = Color(0xFF64B5F6)
    val BlueDark = Color(0xFF1976D2)
    val BlueContainer = Color(0xFFE3F0FC)

    val Red = Color(0xFFEF5350)
    val RedDark = Color(0xFFD32F2F)
    val RedContainer = Color(0xFFFDE4E6)

    val Orange = Color(0xFFF57C00)
}