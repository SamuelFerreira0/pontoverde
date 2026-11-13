package com.example.pontoverde.ui.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pontoverde.ui.theme.EcoBottomBar
import com.example.pontoverde.ui.theme.EcoTab
import com.example.pontoverde.ui.theme.ScreenHeader
import com.example.pontoverde.ui.theme.EcoColors
import com.example.pontoverde.ui.theme.PontoverdeTheme

private const val MAX_DESCRIPTION = 180

enum class ProblemType(
    val label: String,
    val summary: String,
    val icon: ImageVector,
) {
    Full("Cheio", "Ecoponto cheio", Icons.Outlined.DeleteSweep),
    Damaged("Danificado", "Ecoponto danificado", Icons.Outlined.Construction),
    Dirty("Sujo", "Ecoponto sujo", Icons.Outlined.CleaningServices),
    Missing("Em falta", "Ecoponto em falta", Icons.Outlined.LocationOff),
}

@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    locationName: String = "A tua localização",
    city: String = "Lisboa",
    onUseNearest: () -> Unit = {},
    onAddPhoto: () -> Unit = {},
    onSubmit: (problem: ProblemType, description: String) -> Unit = { _, _ -> },
) {
    var selected by rememberSaveable { mutableStateOf(ProblemType.Full) }
    var description by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        ScreenHeader(
            overline = "Ajuda a tua comunidade",
            title = "Sinalizar ecoponto",
            titleSize = 26.sp,
        )

        Spacer(Modifier.height(20.dp))

        // ---- Step 1 ---------------------------------------------------------
        StepTitle(1, "Escolhe o ecoponto", "Indica onde encontraste o problema")
        Spacer(Modifier.height(12.dp))
        MapPreview(locationName = locationName, city = city)
        Spacer(Modifier.height(12.dp))
        UseNearestButton(onClick = onUseNearest)

        Spacer(Modifier.height(24.dp))

        // ---- Step 2 ---------------------------------------------------------
        StepTitle(2, "Qual é o problema?", "Seleciona uma opção")
        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ProblemType.entries.chunked(2).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    rowItems.forEach { type ->
                        ProblemChip(
                            type = type,
                            selected = type == selected,
                            onClick = { selected = type },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        // ---- Description ----------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Descrição",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = EcoColors.TextPrimary,
            )
            Text(text = "Opcional", fontSize = 10.sp, color = EcoColors.TextMuted)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { if (it.length <= MAX_DESCRIPTION) description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp),
            placeholder = {
                Text(
                    text = "Acrescenta detalhes que nos possam ajudar...",
                    fontSize = 12.sp,
                    color = EcoColors.TextMuted,
                )
            },
            textStyle = TextStyle(fontSize = 13.sp, color = EcoColors.TextPrimary),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = EcoColors.Green,
                unfocusedBorderColor = EcoColors.Outline,
                cursorColor = EcoColors.Green,
            ),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "${description.length}/$MAX_DESCRIPTION",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            fontSize = 10.sp,
            color = EcoColors.TextMuted,
        )

        Spacer(Modifier.height(18.dp))

        // ---- Photo ----------------------------------------------------------
        AddPhotoCard(onClick = onAddPhoto)

        Spacer(Modifier.height(20.dp))

        // ---- Submit ---------------------------------------------------------
        Button(
            onClick = { onSubmit(selected, description) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = EcoColors.Green.copy(alpha = 0.3f),
                    spotColor = EcoColors.Green.copy(alpha = 0.4f),
                ),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EcoColors.Green,
                contentColor = Color.White,
            ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.size(10.dp))
            Text(
                text = "Enviar sinalização",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Pieces
// ---------------------------------------------------------------------------

@Composable
private fun StepTitle(number: Int, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(EcoColors.Green, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.size(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = EcoColors.TextPrimary,
            )
            Text(text = subtitle, fontSize = 11.sp, color = EcoColors.TextSecondary)
        }
    }
}

/**
 * Static "map" placeholder drawn with a Canvas (swap for Google Maps / MapLibre later).
 */
@Composable
private fun MapPreview(locationName: String, city: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(138.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFDDE4DD)),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            // Light "blocks"
            drawRect(
                color = Color.White.copy(alpha = 0.25f),
                topLeft = Offset(w * 0.55f, 0f),
                size = Size(w * 0.45f, h * 0.5f),
            )
            // Roads
            drawLine(
                color = Color.White,
                start = Offset(0f, h * 0.15f),
                end = Offset(w * 0.55f, h * 0.85f),
                strokeWidth = 30.dp.toPx(),
            )
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(w * 0.35f, 0f),
                end = Offset(w, h * 0.6f),
                strokeWidth = 12.dp.toPx(),
            )
            drawLine(
                color = Color.White.copy(alpha = 0.7f),
                start = Offset(0f, h * 0.85f),
                end = Offset(w, h * 0.65f),
                strokeWidth = 7.dp.toPx(),
            )
        }

        // Pin
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 24.dp)
                .size(40.dp)
                .shadow(6.dp, CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = EcoColors.Green,
                modifier = Modifier.size(22.dp),
            )
        }

        // Location card
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(14.dp))
                .background(Color.White, RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.MyLocation,
                contentDescription = null,
                tint = EcoColors.Green,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.size(12.dp))
            Column {
                Text(
                    text = locationName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoColors.TextPrimary,
                )
                Text(text = city, fontSize = 11.sp, color = EcoColors.TextSecondary)
            }
        }
    }
}

@Composable
private fun UseNearestButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(shape)
            .background(EcoColors.GreenSurface)
            .border(1.dp, EcoColors.GreenContainer, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.NearMe,
            contentDescription = null,
            tint = EcoColors.TextSecondary,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = "Usar o mais próximo",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = EcoColors.Green,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = EcoColors.GreenDark,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun ProblemChip(
    type: ProblemType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(if (selected) EcoColors.GreenSurface else Color.White)
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) EcoColors.Green else EcoColors.Outline,
                shape = shape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = type.icon,
            contentDescription = null,
            tint = if (selected) EcoColors.GreenDark else EcoColors.TextSecondary,
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = type.label,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) EcoColors.GreenDark else EcoColors.TextPrimary,
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Selecionado",
                tint = EcoColors.Green,
                modifier = Modifier.size(20.dp),
            )
        } else {
            Spacer(Modifier.size(20.dp))
        }
    }
}

@Composable
private fun AddPhotoCard(onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(EcoColors.GreenSurface)
            .dashedBorder(color = Color(0xFFB7D9B9), cornerRadius = 16.dp)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(EcoColors.GreenContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.AddAPhoto,
                contentDescription = null,
                tint = EcoColors.GreenDark,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(Modifier.size(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = "Adicionar fotografia",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = EcoColors.TextPrimary,
            )
            Text(
                text = "Ajuda-nos a resolver mais depressa",
                fontSize = 11.sp,
                color = EcoColors.TextSecondary,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = EcoColors.TextSecondary,
            modifier = Modifier.size(22.dp),
        )
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: Dp,
    strokeWidth: Dp = 1.5.dp,
): Modifier = this.drawBehind {
    val sw = strokeWidth.toPx()
    drawRoundRect(
        color = color,
        topLeft = Offset(sw / 2, sw / 2),
        size = Size(size.width - sw, size.height - sw),
        cornerRadius = CornerRadius(cornerRadius.toPx()),
        style = Stroke(
            width = sw,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(10.dp.toPx(), 7.dp.toPx()),
            ),
        ),
    )
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReportScreenPreview() {
    PontoverdeTheme {
        Scaffold(
            containerColor = EcoColors.Background,
            bottomBar = { EcoBottomBar(selected = EcoTab.Report, onSelect = {}) },
        ) { padding ->
            ReportScreen(modifier = Modifier.padding(padding))
        }
    }
}