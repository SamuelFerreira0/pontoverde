package com.example.pontoverde.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pontoverde.ui.theme.PontoverdeTheme

private val Verde = Color(0xFF2E7D32)
private val VerdeEscuro = Color(0xFF1B5E20)
private val VerdeClaro = Color(0xFFE8F5E9)
private val Texto = Color(0xFF1C1B1F)
private val Cinza = Color(0xFF68646B)
private val AmareloClaro = Color(0xFFFFF2BD)

enum class TipoEcoponto(val nome: String, val cor: Color) {
    AMARELO("Amarelo", Color(0xFFFBC02D)),
    AZUL("Azul", Color(0xFF1976D2)),
    VERDE("Verde", Color(0xFF388E3C)),
    PILHAO("Pilhão", Color(0xFFD32F2F)),
    ECOCENTRO("Ecocentro", Color(0xFFF57C00))
}

data class EcopontoUi(
    val tipo: TipoEcoponto,
    val rua: String,
    val detalhe: String,
    val distancia: String,
    val tempoAPe: String,
    val disponivel: Boolean
)

private class MarcadorFalso(
    val tipo: TipoEcoponto,
    val icone: ImageVector,
    val x: Float,
    val y: Float
)

private val ecopontoExemplo = EcopontoUi(
    TipoEcoponto.AMARELO, "Rua Alexandre Herculano", "Ecoponto junto ao n.º 42",
    "120 m", "2 min", true
)

private val marcadoresFalsos = listOf(
    MarcadorFalso(TipoEcoponto.AMARELO, Icons.Outlined.Delete, 0.68f, 0.25f),
    MarcadorFalso(TipoEcoponto.AZUL, Icons.Outlined.Delete, 0.20f, 0.36f),
    MarcadorFalso(TipoEcoponto.VERDE, Icons.Outlined.Delete, 0.74f, 0.52f),
    MarcadorFalso(TipoEcoponto.PILHAO, Icons.Outlined.BatteryAlert, 0.29f, 0.65f),
    MarcadorFalso(TipoEcoponto.ECOCENTRO, Icons.Outlined.Recycling, 0.84f, 0.69f)
)

@Composable
fun MapScreen(
    ecoponto: EcopontoUi = ecopontoExemplo,
    onIniciarRota: () -> Unit = {},
    onSinalizar: () -> Unit = {},
    onCentrar: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Por agora o filtro vive aqui; na fase 2 passa para o MapViewModel
    var filtro by remember { mutableStateOf<TipoEcoponto?>(TipoEcoponto.AMARELO) }

    CompositionLocalProvider(LocalContentColor provides Texto) {
        BoxWithConstraints(modifier.fillMaxSize().background(Color(0xFFE7EBE4))) {
            val largura = maxWidth
            val altura = maxHeight
            val escala = largura.value / 412f

            MapaFalso(escala)

            marcadoresFalsos.forEach { m ->
                Pin(
                    m,
                    Modifier.offset(x = largura * m.x - 19.dp, y = altura * m.y - 22.dp)
                )
            }

            // Posição do utilizador
            Box(
                Modifier
                    .offset(x = (97 * escala).dp, y = 436.dp)
                    .size(24.dp)
                    .drawBehind {
                        drawCircle(Color(0xFF1976D2).copy(alpha = 0.18f), radius = 22.dp.toPx())
                    }
                    .background(Color(0xFF1976D2), CircleShape)
                    .border(5.dp, Color.White, CircleShape)
            )

            // Botão de centrar
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-16).dp, y = altura * 0.466f)
                    .size(44.dp)
                    .shadow(6.dp, RoundedCornerShape(15.dp))
                    .background(Color.White, RoundedCornerShape(15.dp))
                    .clickable(onClick = onCentrar),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.MyLocation, "Mostrar localização", tint = Verde)
            }

            // Barra de pesquisa e filtros
            Column(
                Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(top = 10.dp)
            ) {
                BarraPesquisa(onCentrar, Modifier.padding(horizontal = 14.dp))
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp)
                ) {
                    items(TipoEcoponto.entries) { tipo ->
                        ChipFiltro(tipo, filtro == tipo) {
                            filtro = if (filtro == tipo) null else tipo
                        }
                    }
                }
            }

            PainelEcoponto(
                ecoponto, onIniciarRota, onSinalizar,
                Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

/** Fundo desenhado que imita o mapa do mockup. Na fase 2 é substituído pelo Google Maps. */
@Composable
private fun BoxWithConstraintsScope.MapaFalso(escala: Float) {
    Canvas(Modifier.fillMaxSize()) {
        val u = 1.dp.toPx()
        drawRect(Color(0xFFE5E9E1))

        // grelha
        val linha = Color(0xFFD9DED5).copy(alpha = 0.35f)
        var x = 0f
        while (x < size.width) { drawLine(linha, Offset(x, 0f), Offset(x, size.height), u); x += 48 * u }
        var y = 0f
        while (y < size.height) { drawLine(linha, Offset(0f, y), Offset(size.width, y), u); y += 48 * u }

        // ruas
        val rua = Color.White
        drawLine(rua, Offset(size.width * 0.22f, 0f), Offset(size.width * 0.22f, size.height), 20 * u)
        drawLine(rua, Offset(size.width * 0.70f, 0f), Offset(size.width * 0.70f, size.height), 18 * u)
        drawLine(rua, Offset(0f, size.height * 0.16f), Offset(size.width, size.height * 0.30f), 16 * u)
        drawLine(rua, Offset(0f, size.height * 0.50f), Offset(size.width, size.height * 0.40f), 14 * u)
        drawLine(rua, Offset(0f, size.height * 0.30f), Offset(size.width * 0.5f, size.height * 0.62f), 12 * u)
        drawLine(rua, Offset(size.width * 0.5f, size.height * 0.62f), Offset(size.width, size.height * 0.80f), 12 * u)

        // rota (coordenadas do SVG do mockup, viewBox 412 x 500, deslocada 90)
        val k = u * escala
        fun p(px: Float, py: Float) = Offset(px * k, 90 * u + py * k)
        val rota = Path().apply {
            moveTo(p(108f, 355f).x, p(108f, 355f).y)
            cubicTo(p(128f, 312f).x, p(128f, 312f).y, p(145f, 300f).x, p(145f, 300f).y, p(173f, 294f).x, p(173f, 294f).y)
            cubicTo(p(201f, 288f).x, p(201f, 288f).y, p(232f, 282f).x, p(232f, 282f).y, p(250f, 244f).x, p(250f, 244f).y)
            cubicTo(p(268f, 206f).x, p(268f, 206f).y, p(278f, 204f).x, p(278f, 204f).y, p(304f, 190f).x, p(304f, 190f).y)
        }
        drawPath(rota, Verde, style = Stroke(width = 7 * k, cap = StrokeCap.Round))
    }

    // Parques
    Box(
        Modifier
            .offset(x = (-15).dp, y = 155.dp)
            .size(95.dp, 80.dp)
            .background(
                Color(0xFFCFE3CD),
                RoundedCornerShape(topStart = 20.dp, topEnd = 35.dp, bottomEnd = 28.dp, bottomStart = 10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Outlined.Park, null, tint = Color(0xFF75A879))
    }
    Box(
        Modifier
            .align(Alignment.TopEnd)
            .offset(x = 18.dp, y = 345.dp)
            .size(100.dp, 66.dp)
            .background(
                Color(0xFFCFE3CD),
                RoundedCornerShape(topStart = 40.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 28.dp)
            )
    )

    // Nomes das ruas
    val corRua = Color(0xFF8C9189)
    Text(
        "Av. da Liberdade", color = corRua, fontSize = 8.sp, letterSpacing = 0.5.sp,
        modifier = Modifier.offset(x = (138 * escala).dp, y = 222.dp).rotate(-19f)
    )
    Text(
        "Rua do Sol", color = corRua, fontSize = 8.sp, letterSpacing = 0.5.sp,
        modifier = Modifier.offset(x = (41 * escala).dp, y = 333.dp).rotate(29f)
    )
    Text(
        "Praça Verde", color = corRua, fontSize = 8.sp, letterSpacing = 0.5.sp,
        modifier = Modifier.align(Alignment.TopEnd).offset(x = (-12).dp, y = 285.dp).rotate(90f)
    )
}

private val FormaPin = GenericShape { s, _ ->
    moveTo(s.width * 0.50f, s.height)
    lineTo(s.width * 0.07f, s.height * 0.52f)
    lineTo(s.width * 0.07f, s.height * 0.32f)
    lineTo(s.width * 0.17f, s.height * 0.13f)
    lineTo(s.width * 0.34f, s.height * 0.03f)
    lineTo(s.width * 0.56f, s.height * 0.02f)
    lineTo(s.width * 0.76f, s.height * 0.12f)
    lineTo(s.width * 0.91f, s.height * 0.32f)
    lineTo(s.width * 0.92f, s.height * 0.52f)
    close()
}

@Composable
private fun Pin(m: MarcadorFalso, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 38.dp, height = 44.dp)
            .clip(FormaPin)
            .background(m.tipo.cor),
        contentAlignment = Alignment.TopCenter
    ) {
        Icon(
            m.icone, null,
            tint = if (m.tipo == TipoEcoponto.AMARELO) Color(0xFF554300) else Color.White,
            modifier = Modifier.padding(top = 7.dp).size(19.dp)
        )
    }
}

@Composable
private fun BarraPesquisa(onCentrar: () -> Unit, modifier: Modifier = Modifier) {
    val forma = RoundedCornerShape(26.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(8.dp, forma)
            .background(Color.White, forma)
            .padding(start = 16.dp, end = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Outlined.Search, null)
        Text("Ecopontos perto de ti", Modifier.weight(1f), color = Color(0xFF454146), fontSize = 14.sp)
        Box(
            Modifier.size(40.dp).background(VerdeClaro, CircleShape).clickable(onClick = onCentrar),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.MyLocation, "Centrar", tint = Verde)
        }
    }
}

@Composable
private fun ChipFiltro(tipo: TipoEcoponto, selecionado: Boolean, onClick: () -> Unit) {
    val forma = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .height(34.dp)
            .shadow(2.dp, forma)
            .background(if (selecionado) Color(0xFFE2F2E4) else Color.White.copy(alpha = 0.94f), forma)
            .border(1.dp, if (selecionado) Color(0xFF72AA76) else Color(0xFFDEDBDC), forma)
            .clip(forma)
            .clickable(onClick = onClick)
            .padding(horizontal = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(Modifier.size(9.dp).background(tipo.cor, CircleShape))
        Text(
            tipo.nome,
            color = if (selecionado) VerdeEscuro else Color(0xFF4A464A),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun PainelEcoponto(
    ecoponto: EcopontoUi,
    onIniciarRota: () -> Unit,
    onSinalizar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 290.dp)
            .shadow(16.dp, forma)
            .background(Color.White, forma)
            .padding(start = 20.dp, end = 20.dp, top = 11.dp, bottom = 19.dp)
    ) {
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 40.dp, height = 4.dp)
                .background(Color(0xFFCBC8C9), RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            // Etiqueta fixa amarela como no mockup
            Text(
                "Mais próximo",
                modifier = Modifier
                    .background(AmareloClaro, RoundedCornerShape(12.dp))
                    .padding(horizontal = 9.dp, vertical = 5.dp),
                color = Color(0xFF5F4A00),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Box(Modifier.size(7.dp).background(Color(0xFF43A047), CircleShape))
            Spacer(Modifier.width(5.dp))
            Text(
                if (ecoponto.disponivel) "Disponível" else "Cheio",
                color = Verde, fontSize = 10.sp, fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(9.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(ecoponto.rua, fontSize = 18.sp, letterSpacing = (-0.3).sp)
                Spacer(Modifier.height(3.dp))
                Text(ecoponto.detalhe, color = Cinza, fontSize = 11.sp)
            }
            Box(
                Modifier.size(42.dp).background(AmareloClaro, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Delete, null, tint = Color(0xFF5C4900))
            }
        }

        Spacer(Modifier.height(13.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F7F3), RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Dado(Icons.Outlined.LocationOn, ecoponto.distancia, "de distância", Modifier.weight(1f))
            Box(
                Modifier.padding(horizontal = 13.dp).size(width = 1.dp, height = 27.dp)
                    .background(Color(0xFFD9DDD7))
            )
            Dado(Icons.AutoMirrored.Outlined.DirectionsWalk, ecoponto.tempoAPe, "a pé", Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BotaoPainel("Iniciar rota", Icons.Outlined.NearMe, true, onIniciarRota, Modifier.weight(0.9f))
            BotaoPainel("Sinalizar problema", Icons.Outlined.ReportProblem, false, onSinalizar, Modifier.weight(1.25f))
        }
    }
}

@Composable
private fun Dado(icone: ImageVector, valor: String, legenda: String, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icone, null, tint = Verde, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
        Column {
            Text(valor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(legenda, color = Cinza, fontSize = 9.sp)
        }
    }
}

@Composable
private fun BotaoPainel(
    texto: String,
    icone: ImageVector,
    primario: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(25.dp)
    val base = modifier.height(46.dp)
    val estilo = if (primario) {
        base.shadow(7.dp, forma, ambientColor = Verde.copy(alpha = 0.22f), spotColor = Verde.copy(alpha = 0.22f))
            .background(Verde, forma)
    } else {
        base.background(Color.White, forma).border(1.5.dp, Color(0xFF79AD7C), forma)
    }
    Row(
        modifier = estilo.clip(forma).clickable(onClick = onClick).padding(horizontal = 11.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, null, tint = if (primario) Color.White else Verde)
        Text(
            texto,
            color = if (primario) Color.White else Verde,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 816)
@Composable
private fun MapScreenPreview() {
    PontoverdeTheme { MapScreen() }
}