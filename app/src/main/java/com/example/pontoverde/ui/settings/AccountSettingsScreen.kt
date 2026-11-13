package com.example.pontoverde.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pontoverde.ui.theme.PontoverdeTheme

private val Verde = Color(0xFF2E7D32)
private val VerdeClaro = Color(0xFFE8F5E9)
private val Fundo = Color(0xFFFBFDF9)
private val Texto = Color(0xFF1C1B1F)
private val Cinza = Color(0xFF68646B)

@Composable
fun AccountSettingsScreen(
    modifier: Modifier = Modifier,
    userName: String = "Ana Martins",
    email: String = "ana.martins@email.com",
    onBack: () -> Unit = {},
    onSave: (name: String) -> Unit = {},
    onChangePassword: () -> Unit = {},
) {
    var nome by rememberSaveable { mutableStateOf(userName) }
    var guardado by rememberSaveable { mutableStateOf(false) }

    CompositionLocalProvider(LocalContentColor provides Texto) {
        Box(modifier.fillMaxSize().background(Fundo)) {
            Column(Modifier.fillMaxSize().statusBarsPadding()) {
                BarraTopo(onBack)
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(17.dp)) {
                        Campo(
                            label = "Nome",
                            value = nome,
                            onValueChange = { nome = it; guardado = false },
                            placeholder = "O teu nome"
                        )
                        Campo(
                            label = "Email",
                            value = email,
                            onValueChange = {},
                            placeholder = "O teu email",
                            readOnly = true,
                            tipo = KeyboardType.Email
                        )
                    }
                    LinhaAlterarPassword(onChangePassword)
                    BotaoGuardar {
                        onSave(nome.trim().ifEmpty { userName })
                        guardado = true
                    }
                }
            }
            if (guardado) {
                MensagemGuardado(
                    Modifier.align(Alignment.BottomCenter).padding(horizontal = 24.dp, vertical = 28.dp)
                )
            }
        }
    }
}

@Composable
private fun BarraTopo(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 74.dp)
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFE4E2E2), CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
        }
        Text("Definições da conta", fontSize = 25.sp, letterSpacing = (-0.5).sp)
    }
}

@Composable
private fun Campo(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false,
    tipo: KeyboardType = KeyboardType.Text,
) {
    var focado by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(16.dp)
    val corBorda = when {
        readOnly -> Color(0xFFE1DFDF)
        focado -> Verde
        else -> Color(0xFFD8D6D7)
    }
    Column {
        Text(
            label,
            modifier = Modifier.padding(start = 2.dp, bottom = 7.dp),
            color = Color(0xFF403C40),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(if (readOnly) Color(0xFFF2F2F2) else Color.White, forma)
                .border(if (focado && !readOnly) 1.5.dp else 1.dp, corBorda, forma)
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(placeholder, color = Color(0xFF9A969A), fontSize = 13.sp)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                singleLine = true,
                textStyle = TextStyle(
                    color = if (readOnly) Color(0xFF7F7B7F) else Texto,
                    fontSize = 13.sp
                ),
                cursorBrush = SolidColor(Verde),
                keyboardOptions = KeyboardOptions(keyboardType = tipo),
                modifier = Modifier.fillMaxWidth().onFocusChanged { focado = it.isFocused }
            )
        }
    }
}

@Composable
private fun LinhaAlterarPassword(onClick: () -> Unit) {
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .background(Color.White, forma)
            .border(1.dp, Color(0xFFE4E2E2), forma)
            .clip(forma)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Box(
            Modifier.size(40.dp).background(VerdeClaro, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.LockReset, contentDescription = null, tint = Verde)
        }
        Column(Modifier.weight(1f)) {
            Text("Alterar palavra-passe", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(
                "Enviamos-te um email para a redefinires",
                modifier = Modifier.padding(top = 4.dp),
                color = Cinza,
                fontSize = 9.sp
            )
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF8A868A),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun BotaoGuardar(onClick: () -> Unit) {
    val forma = RoundedCornerShape(25.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                8.dp, forma,
                ambientColor = Verde.copy(alpha = 0.22f),
                spotColor = Verde.copy(alpha = 0.22f)
            )
            .background(Verde, forma)
            .clip(forma)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Save, contentDescription = null, tint = Color.White)
        Text("Guardar alterações", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MensagemGuardado(modifier: Modifier = Modifier) {
    val forma = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .shadow(10.dp, forma)
            .background(Color(0xFF29362A), forma)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF9FD7A2),
            modifier = Modifier.size(20.dp)
        )
        Text("Alterações guardadas", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 816)
@Composable
private fun AccountSettingsScreenPreview() {
    PontoverdeTheme { AccountSettingsScreen() }
}