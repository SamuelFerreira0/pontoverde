package com.example.pontoverde.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pontoverde.ui.theme.Overline
import com.example.pontoverde.ui.theme.EcoColors
import com.example.pontoverde.ui.theme.PontoverdeTheme

@Composable
fun ReportSentScreen(
    modifier: Modifier = Modifier,
    problem: ProblemType = ProblemType.Full,
    location: String = "Rua Alexandre Herculano",
    onBackToMap: () -> Unit = {},
    onGoToCamera: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEFF8EF), Color.White)))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(96.dp))

        SuccessBadge()

        Spacer(Modifier.height(36.dp))

        Overline(text = "Sinalização enviada")

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Obrigado!",
            fontSize = 34.sp,
            color = EcoColors.TextPrimary,
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "A tua sinalização foi enviada. Juntos estamos a tornar a cidade mais limpa e sustentável.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = EcoColors.TextSecondary,
        )

        Spacer(Modifier.height(28.dp))

        ReportSummaryCard(problem = problem, location = location)

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onBackToMap,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = EcoColors.Green.copy(alpha = 0.3f),
                    spotColor = EcoColors.Green.copy(alpha = 0.45f),
                ),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EcoColors.Green,
                contentColor = Color.White,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Map,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Voltar ao mapa",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(Modifier.height(4.dp))

        TextButton(onClick = onGoToCamera) {
            Text(
                text = "Ir para a câmara",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = EcoColors.Green,
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

// ---------------------------------------------------------------------------
// Pieces
// ---------------------------------------------------------------------------

@Composable
private fun SuccessBadge() {
    Box(modifier = Modifier.size(146.dp), contentAlignment = Alignment.Center) {
        // Soft halo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            EcoColors.Green.copy(alpha = 0.18f),
                            EcoColors.Green.copy(alpha = 0.07f),
                        ),
                    ),
                ),
        )

        // Confetti dots
        ConfettiDot(color = EcoColors.Yellow, size = 7.dp, modifier = Modifier.align(Alignment.TopStart).offset(x = 12.dp, y = 49.dp))
        ConfettiDot(color = Color(0xFF1E7BE0), size = 9.dp, modifier = Modifier.align(Alignment.TopStart).offset(x = 121.dp, y = 29.dp))
        ConfettiDot(color = EcoColors.Orange, size = 8.dp, modifier = Modifier.align(Alignment.TopStart).offset(x = 124.dp, y = 113.dp))

        // White ring + green check
        Box(
            modifier = Modifier
                .size(86.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    ambientColor = EcoColors.Green.copy(alpha = 0.3f),
                    spotColor = EcoColors.Green.copy(alpha = 0.3f),
                )
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(EcoColors.Green, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Enviado",
                    tint = Color.White,
                    modifier = Modifier.size(42.dp),
                )
            }
        }
    }
}

@Composable
private fun ConfettiDot(color: Color, size: Dp, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(size).background(color, CircleShape))
}

@Composable
private fun ReportSummaryCard(problem: ProblemType, location: String) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color(0x14000000),
                spotColor = Color(0x1F000000),
            )
            .background(Color.White, shape)
            .border(1.dp, EcoColors.Outline, shape)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(EcoColors.YellowContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = problem.icon,
                contentDescription = null,
                tint = EcoColors.YellowDark,
                modifier = Modifier.size(24.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = problem.summary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = EcoColors.TextPrimary,
            )
            Spacer(Modifier.height(4.dp))
            Text(text = location, fontSize = 11.sp, color = EcoColors.TextSecondary)
        }

        Icon(
            imageVector = Icons.Rounded.Verified,
            contentDescription = null,
            tint = EcoColors.Green,
            modifier = Modifier.size(24.dp),
        )
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReportSentScreenPreview() {
    PontoverdeTheme { ReportSentScreen() }
}