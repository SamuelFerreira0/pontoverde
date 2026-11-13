package com.example.pontoverde.ui.result

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pontoverde.data.PassoDeposicao
import com.example.pontoverde.data.WasteInfo
import com.example.pontoverde.ui.map.TipoEcoponto
import com.example.pontoverde.ui.theme.EcoColors
import com.example.pontoverde.ui.theme.LeafBadge
import com.example.pontoverde.ui.theme.PontoverdeTheme
import com.example.pontoverde.ui.theme.ecoCard
import com.example.pontoverde.viewmodel.ResultUiState
import com.example.pontoverde.viewmodel.ResultViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

// O ViewModel tem de ser o mesmo que recebe o resultado da câmara (criado no NavHost).
@Composable
fun ResultScreen(
    viewModel: ResultViewModel,
    onBack: () -> Unit,
    onVerEcoponto: () -> Unit,
    onTentarOutraVez: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ResultContent(
        state = state,
        onBack = onBack,
        onVerEcoponto = onVerEcoponto,
        onSave = viewModel::save,
        onTentarOutraVez = onTentarOutraVez,
        modifier = modifier,
    )
}

@Composable
private fun ResultContent(
    state: ResultUiState,
    onBack: () -> Unit,
    onVerEcoponto: () -> Unit,
    onSave: () -> Unit,
    onTentarOutraVez: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbar = remember { SnackbarHostState() }
    val saved = (state as? ResultUiState.Recognized)?.saved == true
    LaunchedEffect(saved) {
        if (saved) snackbar.showSnackbar("Guardado no histórico")
    }

    Box(modifier = modifier.fillMaxSize().background(EcoColors.Background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            TopBar(onBack)
            when (state) {
                is ResultUiState.Recognized ->
                    RecognizedBody(state, onVerEcoponto, onSave, onTentarOutraVez)
                is ResultUiState.NotRecognized ->
                    NotRecognizedBody(state, onTentarOutraVez)
                ResultUiState.Empty -> EmptyBody(onTentarOutraVez)
            }
        }
        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
        )
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
        }
        Text(
            text = "Resultado",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = EcoColors.TextPrimary,
            modifier = Modifier.weight(1f).padding(start = 4.dp),
        )
        LeafBadge()
    }
}

@Composable
private fun RecognizedBody(
    state: ResultUiState.Recognized,
    onVerEcoponto: () -> Unit,
    onSave: () -> Unit,
    onTentarOutraVez: () -> Unit,
) {
    val waste = state.waste
    val (container, content) = waste.ecoponto.colors()

    PhotoHeader(state.photoPath, identified = true)
    Spacer(Modifier.height(16.dp))
    Column(Modifier.fillMaxWidth().ecoCard().padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                BinChip(waste.ecoponto.label(), container, content)
                Spacer(Modifier.height(10.dp))
                Text(
                    text = waste.nome,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = EcoColors.TextPrimary,
                )
            }
            Spacer(Modifier.width(12.dp))
            ConfidenceBox(waste.confidence)
        }
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = EcoColors.Outline)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Como depositar",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = EcoColors.TextPrimary,
        )
        Spacer(Modifier.height(12.dp))
        waste.passos.forEachIndexed { index, passo ->
            StepRow(index + 1, passo, STEP_ICONS[index % STEP_ICONS.size], container, content)
            if (index < waste.passos.lastIndex) Spacer(Modifier.height(14.dp))
        }
    }

    Spacer(Modifier.height(20.dp))
    PrimaryButton("Ver ecoponto mais próximo", onVerEcoponto, Icons.Filled.Navigation)
    Spacer(Modifier.height(12.dp))
    OutlinedButton(
        onClick = onSave,
        enabled = !state.saved,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(1.5.dp, if (state.saved) EcoColors.Outline else EcoColors.Green),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = EcoColors.Green,
            disabledContentColor = EcoColors.TextMuted,
        ),
    ) {
        if (state.saved) {
            Text("Guardado ✓", fontWeight = FontWeight.Bold)
        } else {
            Icon(Icons.Outlined.BookmarkAdd, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Guardar no histórico", fontWeight = FontWeight.Bold)
        }
    }
    Spacer(Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("Não é isto? ", fontSize = 12.sp, color = EcoColors.TextMuted)
        Text(
            text = "Tentar outra vez",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = EcoColors.Green,
            modifier = Modifier.clickable(onClick = onTentarOutraVez),
        )
    }
    Spacer(Modifier.height(24.dp))
}

@Composable
private fun NotRecognizedBody(state: ResultUiState.NotRecognized, onTentarOutraVez: () -> Unit) {
    PhotoHeader(state.photoPath, identified = false)
    Spacer(Modifier.height(16.dp))
    Column(Modifier.fillMaxWidth().ecoCard().padding(20.dp)) {
        Text(
            text = "Não reconhecido",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = EcoColors.TextPrimary,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Não conseguimos identificar este objeto. Tenta outra vez com mais luz e o objeto bem enquadrado.",
            fontSize = 14.sp,
            color = EcoColors.TextSecondary,
        )
    }
    Spacer(Modifier.height(20.dp))
    PrimaryButton("Tentar outra vez", onTentarOutraVez)
    Spacer(Modifier.height(24.dp))
}

@Composable
private fun EmptyBody(onTirarFotografia: () -> Unit) {
    Spacer(Modifier.height(80.dp))
    Text(
        text = "Ainda não há nenhuma fotografia para mostrar.",
        fontSize = 16.sp,
        color = EcoColors.TextSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(20.dp))
    PrimaryButton("Tirar fotografia", onTirarFotografia)
}

@Composable
private fun PhotoHeader(photoPath: String, identified: Boolean) {
    val photo by produceState<ImageBitmap?>(null, photoPath) {
        value = withContext(Dispatchers.IO) {
            BitmapFactory.decodeFile(photoPath)?.asImageBitmap()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(EcoColors.GreenContainer),
    ) {
        photo?.let {
            Image(
                bitmap = it,
                contentDescription = "Fotografia do resíduo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (identified) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text("Identificado", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun BinChip(label: String, container: Color, content: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(container)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Delete, contentDescription = null, tint = content, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, color = content, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ConfidenceBox(confidence: Float) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(EcoColors.GreenContainer)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "${(confidence * 100).roundToInt()}%",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = EcoColors.Green,
        )
        Text(
            text = "CONFIANÇA",
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold,
            color = EcoColors.Green,
        )
    }
}

@Composable
private fun StepRow(number: Int, passo: PassoDeposicao, icon: ImageVector, container: Color, content: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(26.dp).clip(CircleShape).background(EcoColors.Green),
            contentAlignment = Alignment.Center,
        ) {
            Text("$number", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(container),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(passo.titulo, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = EcoColors.TextPrimary)
            Text(passo.descricao, fontSize = 12.sp, color = EcoColors.TextSecondary)
        }
    }
}

@Composable
private fun PrimaryButton(text: String, onClick: () -> Unit, icon: ImageVector? = null) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = EcoColors.Green,
            contentColor = Color.White,
        ),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.Bold)
    }
}

private val STEP_ICONS = listOf(Icons.Filled.WaterDrop, Icons.Filled.Compress, Icons.Filled.Recycling)

private fun TipoEcoponto.colors(): Pair<Color, Color> = when (this) {
    TipoEcoponto.AMARELO -> EcoColors.YellowContainer to EcoColors.YellowDark
    TipoEcoponto.AZUL -> EcoColors.BlueContainer to EcoColors.BlueDark
    TipoEcoponto.VERDE -> EcoColors.GreenContainer to EcoColors.GreenDark
    TipoEcoponto.PILHAO -> EcoColors.RedContainer to EcoColors.RedDark
    TipoEcoponto.ECOCENTRO -> Color(0xFFFFE8CC) to EcoColors.Orange
}

private fun TipoEcoponto.label(): String = when (this) {
    TipoEcoponto.PILHAO -> "Pilhão"
    TipoEcoponto.ECOCENTRO -> "Ecocentro"
    else -> "Ecoponto ${nome.lowercase()}"
}

private val previewWaste = WasteInfo(
    nome = "Garrafa de plástico",
    ecoponto = TipoEcoponto.AMARELO,
    passos = listOf(
        PassoDeposicao("Esvazia", "Retira todo o conteúdo"),
        PassoDeposicao("Espalma", "Ocupa menos espaço"),
        PassoDeposicao("Deposita", "Sem tampa, no amarelo"),
    ),
    confidence = 0.96f,
)

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun ResultRecognizedPreview() {
    PontoverdeTheme {
        ResultContent(ResultUiState.Recognized("", previewWaste), {}, {}, {}, {})
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun ResultSavedPreview() {
    PontoverdeTheme {
        ResultContent(ResultUiState.Recognized("", previewWaste, saved = true), {}, {}, {}, {})
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun ResultNotRecognizedPreview() {
    PontoverdeTheme {
        ResultContent(ResultUiState.NotRecognized(""), {}, {}, {}, {})
    }
}
