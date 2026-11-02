package com.example.pontoverde.ui.permissions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pontoverde.ui.theme.PontoverdeTheme

private val Verde = Color(0xFF2E7D32)
private val VerdeEscuro = Color(0xFF1B5E20)
private val VerdeClaro = Color(0xFFE8F5E9)
private val Fundo = Color(0xFFFBFDF9)
private val Texto = Color(0xFF1C1B1F)
private val Cinza = Color(0xFF68646B)
private val Vermelho = Color(0xFFD32F2F)

@Composable
fun PermissionsScreen(
    onPermitir: () -> Unit = {},
    // O link "Simular permissão negada" é só do protótipo. Fica escondido (null)
    // e só aparece se alguém passar uma função.
    onSimularNegada: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalContentColor provides Texto) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Fundo)
                .drawBehind {
                    drawCircle(
                        color = Color(0xFFDCEFE0),
                        radius = 145.dp.toPx(),
                        center = Offset(size.width / 2f, size.height * 0.23f)
                    )
                }
                .statusBarsPadding()
                .padding(start = 28.dp, end = 28.dp, top = 25.dp, bottom = 28.dp)
        ) {
            Ilustracao()

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "PERMISSÕES",
                    color = Verde,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(7.dp))
                Text("Para funcionar bem", fontSize = 27.sp, letterSpacing = (-0.6).sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Precisamos de acesso à câmara e localização enquanto usas a aplicação.",
                    modifier = Modifier.widthIn(max = 320.dp),
                    color = Cinza,
                    fontSize = 12.sp,
                    lineHeight = 17.4.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                PermissaoItem(
                    Icons.Outlined.PhotoCamera, "Câmara", "Identificar objetos",
                    Modifier.weight(1f).fillMaxHeight()
                )
                PermissaoItem(
                    Icons.Outlined.LocationOn, "Localização", "Encontrar ecopontos",
                    Modifier.weight(1f).fillMaxHeight()
                )
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(
                        8.dp, RoundedCornerShape(25.dp),
                        ambientColor = Verde.copy(alpha = 0.22f),
                        spotColor = Verde.copy(alpha = 0.22f)
                    )
                    .background(Verde, RoundedCornerShape(25.dp))
                    .clickable(onClick = onPermitir)
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.VerifiedUser, null, tint = Color.White)
                Text("Permitir e continuar", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            if (onSimularNegada != null) {
                Text(
                    "Simular permissão negada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onSimularNegada)
                        .padding(top = 10.dp, bottom = 5.dp),
                    color = Color(0xFF817D81),
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                )
            }
            Text(
                "Podes alterar estas permissões a qualquer momento.",
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                color = Cinza,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Ilustracao() {
    Box(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        val formaTelemovel = RoundedCornerShape(31.dp)
        Box(
            modifier = Modifier
                .size(width = 130.dp, height = 215.dp)
                .shadow(
                    20.dp, formaTelemovel,
                    ambientColor = Color(0xFF225726), spotColor = Color(0xFF225726)
                )
                .background(
                    Brush.linearGradient(listOf(Color(0xFF4F9B54), VerdeEscuro)),
                    formaTelemovel
                )
                .border(8.dp, Color.White, formaTelemovel),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 45.dp)
                    .size(width = 44.dp, height = 5.dp)
                    .background(Color.White.copy(alpha = 0.78f), RoundedCornerShape(4.dp))
            )
            Icon(Icons.Outlined.Recycling, null, tint = Color.White, modifier = Modifier.size(53.dp))
        }

        FlutuanteIcone(
            icone = Icons.Outlined.PhotoCamera,
            cor = Verde,
            rotacao = -7f,
            modifier = Modifier.align(Alignment.BottomStart).offset(x = 43.dp, y = (-38).dp)
        )
        FlutuanteIcone(
            icone = Icons.Filled.LocationOn,
            cor = Vermelho,
            rotacao = 7f,
            modifier = Modifier.align(Alignment.TopEnd).offset(x = (-42).dp, y = 37.dp)
        )
    }
}

@Composable
private fun FlutuanteIcone(
    icone: ImageVector,
    cor: Color,
    rotacao: Float,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .rotate(rotacao)
            .size(54.dp)
            .shadow(9.dp, forma, ambientColor = Color(0xFF224B25), spotColor = Color(0xFF224B25))
            .background(Color.White, forma),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, null, tint = cor)
    }
}

@Composable
private fun PermissaoItem(
    icone: ImageVector,
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .heightIn(min = 68.dp)
            .background(Color.White, forma)
            .border(1.dp, Color(0xFFE2E8E2), forma)
            .padding(9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            Modifier.size(34.dp).background(VerdeClaro, RoundedCornerShape(11.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icone, null, tint = Verde)
        }
        Column(Modifier.weight(1f)) {
            Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(subtitulo, fontSize = 10.sp, lineHeight = 12.5.sp, color = Cinza)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
            tint = Color(0xFF8D898D), modifier = Modifier.size(17.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 816)
@Composable
private fun PermissionsScreenPreview() {
    PontoverdeTheme { PermissionsScreen() }
}