package com.example.pontoverde.ui.auth

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ecrã de Boas-vindas: o primeiro ecrã para quem ainda não tem sessão.
 * Em cima, a parte verde com a ilustração; em baixo, a folha branca
 * com os botões "Criar conta" e "Entrar".
 */
@Composable
fun BoasVindasScreen(
    onCriarConta: () -> Unit,
    onEntrar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(CoresAuth.Verde, Color(0xFF256B2A)))
            )
    ) {
        // ---------- Parte verde ----------
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Círculos decorativos do fundo
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.07f),
                    radius = 90.dp.toPx(),
                    center = Offset(size.width - 20.dp.toPx(), 30.dp.toPx())
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.06f),
                    radius = 140.dp.toPx(),
                    center = Offset(size.width, size.height * 0.75f)
                )
                drawCircle(
                    color = Color.Black.copy(alpha = 0.05f),
                    radius = 110.dp.toPx(),
                    center = Offset(0f, size.height * 0.7f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                // Cabeçalho: logótipo, nome e etiqueta ODS 11
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LogoPontoVerde(
                        tamanho = 40.dp,
                        corFundo = Color.White,
                        corSimbolo = CoresAuth.Verde,
                        cantos = 12.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "PontoVerde",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "ODS 11",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(50))
                            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(Modifier.height(28.dp))

                Text(
                    text = "SEPARA MELHOR. VIVE MAIS VERDE.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "O ecoponto certo,\nsempre à mão.",
                    color = Color.White,
                    fontSize = 32.sp,
                    lineHeight = 38.sp
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Ilustracao()
                }
            }
        }

        // ---------- Folha branca de baixo ----------
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 20.dp)
            ) {
                // Pequena barra cinzenta no topo da folha
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(width = 40.dp, height = 4.dp)
                        .background(CoresAuth.Borda, RoundedCornerShape(50))
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Começa a reciclar melhor",
                    color = CoresAuth.Texto,
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Cria uma conta para guardares o teu impacto ou entra para continuares onde ficaste.",
                    color = CoresAuth.TextoSecundario,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(28.dp))

                BotaoAuth(
                    texto = "Criar conta",
                    icone = IconesAuth.CriarConta,
                    onClick = onCriarConta
                )
                Spacer(Modifier.height(14.dp))
                BotaoAuth(
                    texto = "Entrar",
                    icone = IconesAuth.Entrar,
                    onClick = onEntrar,
                    preenchido = false
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Ao continuar, aceitas os Termos de utilização e a Política de privacidade.",
                    color = CoresAuth.TextoSecundario,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/** Ilustração do centro: círculos, logótipo e etiquetas a "flutuar" à volta. */
@Composable
private fun Ilustracao() {
    Box(
        modifier = Modifier.size(280.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val raio = size.minDimension / 2
            drawCircle(
                color = Color.White.copy(alpha = 0.25f),
                radius = raio - 4.dp.toPx(),
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.25f),
                radius = raio - 50.dp.toPx(),
                style = Stroke(
                    width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            )
        }

        LogoPontoVerde(
            tamanho = 88.dp,
            corFundo = Color.White,
            corSimbolo = CoresAuth.VerdeEscuro,
            cantos = 24.dp
        )

        Etiqueta(
            texto = "Identifica",
            icone = IconesAuth.Camera,
            modifier = Modifier
                .offset(x = (-85).dp, y = (-30).dp)
                .rotate(-6f)
        )
        Etiqueta(
            texto = "Encontra",
            icone = IconesAuth.Local,
            modifier = Modifier
                .offset(x = 90.dp, y = 50.dp)
                .rotate(5f)
        )

        IconeFlutuante(IconesAuth.Documento, Color(0xFF1E88E5), Modifier.offset(x = 65.dp, y = (-75).dp))
        IconeFlutuante(IconesAuth.Contentor, Color(0xFFFBC02D), Modifier.offset(x = (-75).dp, y = 60.dp))
        IconeFlutuante(IconesAuth.Vidro, Color(0xFF43A047), Modifier.offset(x = 15.dp, y = 105.dp))
    }
}

@Composable
private fun Etiqueta(texto: String, icone: ImageVector, modifier: Modifier = Modifier) {
    val forma = RoundedCornerShape(10.dp)
    Row(
        modifier = modifier
            .shadow(6.dp, forma)
            .background(Color.White, forma)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = CoresAuth.VerdeEscuro, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(texto, color = CoresAuth.VerdeEscuro, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun IconeFlutuante(icone: ImageVector, cor: Color, modifier: Modifier = Modifier) {
    val forma = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .size(40.dp)
            .shadow(6.dp, forma)
            .background(cor, forma)
            .border(2.dp, Color.White, forma),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun BoasVindasScreenPreview() {
    BoasVindasScreen(onCriarConta = {}, onEntrar = {})
}
