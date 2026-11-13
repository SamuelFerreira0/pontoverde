package com.example.pontoverde.ui.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pontoverde.ui.theme.EcoBottomBar
import com.example.pontoverde.ui.theme.EcoTab
import com.example.pontoverde.ui.theme.Overline
import com.example.pontoverde.ui.theme.ScreenHeader
import com.example.pontoverde.ui.theme.ecoCard
import com.example.pontoverde.ui.theme.EcoColors
import com.example.pontoverde.ui.theme.PontoverdeTheme

// ---------------------------------------------------------------------------
// Models + sample data
// ---------------------------------------------------------------------------

data class BinStat(val label: String, val count: Int, val color: Color)

data class RecyclingActivity(
    val title: String,
    val bin: String,
    val time: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconContainer: Color,
)

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

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userName: String = "Ana Martins",
    email: String = "jgmeirafaria@gmail.com",
    stats: List<BinStat> = sampleStats,
    activities: List<RecyclingActivity> = sampleActivities,
) {
    var showEmpty by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(overline = "A tua conta", title = "Perfil")
            Spacer(Modifier.height(4.dp))
        }

        item { ProfileCard(name = userName, email = email) }

        item { MonthlyStatsCard(stats = stats) }

        item {
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = "Atividade recente",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = EcoColors.TextPrimary,
                    )
                    Text(
                        text = "Os teus últimos objetos identificados",
                        fontSize = 11.sp,
                        color = EcoColors.TextSecondary,
                    )
                }
                Text(
                    text = if (showEmpty) "Ver lista" else "Ver vazio",
                    modifier = Modifier.clickable { showEmpty = !showEmpty },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EcoColors.TextSecondary,
                )
            }
        }

        if (showEmpty || activities.isEmpty()) {
            item { EmptyActivity() }
        } else {
            items(activities) { activity -> ActivityRow(activity) }
        }
    }
}

// ---------------------------------------------------------------------------
// Pieces
// ---------------------------------------------------------------------------

@Composable
private fun ProfileCard(name: String, email: String) {
    val initials = name.split(" ")
        .mapNotNull { it.firstOrNull() }
        .take(2)
        .joinToString("")
        .uppercase()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .ecoCard(shape = RoundedCornerShape(24.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(60.dp)) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.Center)
                    .border(3.dp, EcoColors.GreenContainer, CircleShape)
                    .padding(3.dp)
                    .background(
                        Brush.linearGradient(listOf(EcoColors.Green, EcoColors.GreenDark)),
                        CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            // Leaf badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(22.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, EcoColors.Outline, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Eco,
                    contentDescription = null,
                    tint = EcoColors.Green,
                    modifier = Modifier.size(13.dp),
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = EcoColors.TextPrimary,
            )
            Spacer(Modifier.height(2.dp))
            Text(text = email, fontSize = 11.sp, color = EcoColors.TextSecondary)
        }
    }
}

@Composable
private fun MonthlyStatsCard(stats: List<BinStat>) {
    val total = stats.sumOf { it.count }
    val max = stats.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1
    val shape = RoundedCornerShape(28.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.linearGradient(listOf(EcoColors.Green, EcoColors.GreenDark))),
    ) {
        // Decorative ring in the top-right corner
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 36.dp, y = (-36).dp)
                .size(120.dp)
                .border(16.dp, Color.White.copy(alpha = 0.08f), CircleShape),
        )

        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)) {
            Overline(
                text = "Este mês",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 10.sp,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("Reciclaste ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$total objetos")
                    }
                    append(" este mês")
                },
                modifier = Modifier.fillMaxWidth(0.62f),
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            )
            Spacer(Modifier.height(18.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                stats.forEach { stat ->
                    BarColumn(
                        stat = stat,
                        maxCount = max,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun BarColumn(stat: BinStat, maxCount: Int, modifier: Modifier = Modifier) {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }

    val target = if (started) 10.dp + 44.dp * (stat.count.toFloat() / maxCount) else 0.dp
    val fillHeight by animateDpAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 700),
        label = "barHeight",
    )

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stat.count.toString(),
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(62.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.16f)),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fillHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .background(stat.color),
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = stat.label,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ActivityRow(activity: RecyclingActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .ecoCard()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(activity.iconContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = activity.icon,
                contentDescription = null,
                tint = activity.iconTint,
                modifier = Modifier.size(24.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = EcoColors.TextPrimary,
            )
            Spacer(Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = activity.bin, fontSize = 12.sp, color = EcoColors.TextSecondary)
                Text(text = activity.time, fontSize = 10.sp, color = EcoColors.TextMuted)
            }
        }
    }
}

@Composable
private fun EmptyActivity() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .ecoCard()
            .padding(vertical = 32.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Ainda não identificaste nenhum objeto.\nAponta a câmara para começar!",
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            color = EcoColors.TextSecondary,
        )
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    PontoverdeTheme {
        Scaffold(
            containerColor = EcoColors.Background,
            bottomBar = { EcoBottomBar(selected = EcoTab.Profile, onSelect = {}) },
        ) { padding ->
            ProfileScreen(modifier = Modifier.padding(padding))
        }
    }
}