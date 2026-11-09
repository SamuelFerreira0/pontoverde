package com.example.pontoverde.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Peças partilhadas pelos ecrãs de autenticação (Boas-vindas, Entrar, Criar conta).
 * Assim cada ecrã só trata do que é seu e o aspeto fica igual em todos.
 */

/** Cores dos mockups do PontoVerde. */
object CoresAuth {
    val Verde = Color(0xFF2E7D32)
    val VerdeEscuro = Color(0xFF1B5E20)
    val VerdeClaro = Color(0xFFE3F0DC)
    val Fundo = Color(0xFFF6FAF3)
    val Texto = Color(0xFF1C1B1F)
    val TextoSecundario = Color(0xFF5F6368)
    val Borda = Color(0xFFD6DCD3)
    val Erro = Color(0xFFC62828)
}

/**
 * Ícones desenhados a partir do caminho SVG dos Material Icons.
 * Assim não é preciso acrescentar a biblioteca material-icons-extended ao projeto.
 */
object IconesAuth {
    val Voltar = icone("M20,11H7.83l5.59,-5.59L12,4l-8,8 8,8 1.41,-1.41L7.83,13H20v-2z")
    val Entrar = icone("M11,7L9.6,8.4l2.6,2.6H2v2h10.2l-2.6,2.6L11,17l5,-5L11,7zM20,19h-8v2h8c1.1,0 2,-0.9 2,-2V5c0,-1.1 -0.9,-2 -2,-2h-8v2h8V19z")
    val CriarConta = icone("M15,12c2.21,0 4,-1.79 4,-4s-1.79,-4 -4,-4 -4,1.79 -4,4 1.79,4 4,4zM6,10L6,7L4,7v3L1,10v2h3v3h2v-3h3v-2L6,10zM15,14c-2.67,0 -8,1.34 -8,4v2h16v-2c0,-2.66 -5.33,-4 -8,-4z")
    val Mostrar = icone("M12,4.5C7,4.5 2.73,7.61 1,12c1.73,4.39 6,7.5 11,7.5s9.27,-3.11 11,-7.5c-1.73,-4.39 -6,-7.5 -11,-7.5zM12,17c-2.76,0 -5,-2.24 -5,-5s2.24,-5 5,-5 5,2.24 5,5 -2.24,5 -5,5zM12,9c-1.66,0 -3,1.34 -3,3s1.34,3 3,3 3,-1.34 3,-3 -1.34,-3 -3,-3z")
    val Ocultar = icone("M12,7c2.76,0 5,2.24 5,5 0,0.65 -0.13,1.26 -0.36,1.83l2.92,2.92c1.51,-1.26 2.7,-2.89 3.43,-4.75 -1.73,-4.39 -6,-7.5 -11,-7.5 -1.4,0 -2.74,0.25 -3.98,0.7l2.16,2.16C10.74,7.13 11.35,7 12,7zM2,4.27l2.28,2.28 0.46,0.46C3.08,8.3 1.78,10.02 1,12c1.73,4.39 6,7.5 11,7.5 1.55,0 3.03,-0.3 4.38,-0.84l0.42,0.42L19.73,22 21,20.73 3.27,3 2,4.27zM7.53,9.8l1.55,1.55c-0.05,0.21 -0.08,0.43 -0.08,0.65 0,1.66 1.34,3 3,3 0.22,0 0.44,-0.03 0.65,-0.08l1.55,1.55c-0.67,0.33 -1.41,0.53 -2.2,0.53 -2.76,0 -5,-2.24 -5,-5 0,-0.79 0.2,-1.53 0.53,-2.2zM11.84,9.02l3.15,3.15 0.02,-0.16c0,-1.66 -1.34,-3 -3,-3l-0.17,0.01z")
    val Camera = icone("M9,2L7.17,4H4c-1.1,0 -2,0.9 -2,2v12c0,1.1 0.9,2 2,2h16c1.1,0 2,-0.9 2,-2V6c0,-1.1 -0.9,-2 -2,-2h-3.17L15,2H9zM12,17c-2.76,0 -5,-2.24 -5,-5s2.24,-5 5,-5 5,2.24 5,5 -2.24,5 -5,5zM12,15.2c1.77,0 3.2,-1.43 3.2,-3.2S13.77,8.8 12,8.8 8.8,10.23 8.8,12s1.43,3.2 3.2,3.2z")
    val Local = icone("M12,2C8.13,2 5,5.13 5,9c0,5.25 7,13 7,13s7,-7.75 7,-13c0,-3.87 -3.13,-7 -7,-7zM12,11.5c-1.38,0 -2.5,-1.12 -2.5,-2.5s1.12,-2.5 2.5,-2.5 2.5,1.12 2.5,2.5 -1.12,2.5 -2.5,2.5z")
    val Documento = icone("M14,2H6c-1.1,0 -1.99,0.9 -1.99,2L4,20c0,1.1 0.89,2 1.99,2H18c1.1,0 2,-0.9 2,-2V8l-6,-6zM16,18H8v-2h8v2zM16,14H8v-2h8v2zM13,9V3.5L18.5,9H13z")
    val Contentor = icone("M6,19c0,1.1 0.9,2 2,2h8c1.1,0 2,-0.9 2,-2V7H6v12zM19,4h-3.5l-1,-1h-5l-1,1H5v2h14V4z")
    val Vidro = icone("M6,3l0,6c0,2.97 2.16,5.43 5,5.91L11,19L8,19l0,2h8v-2h-3v-4.09c2.84,-0.48 5,-2.94 5,-5.91l0,-6L6,3zM16,8L8,8L8,5h8v3z")
}

private fun icone(caminho: String): ImageVector =
    ImageVector.Builder(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).addPath(
        pathData = PathParser().parsePathString(caminho).toNodes(),
        fill = SolidColor(Color.Black)
    ).build()

/** Quadrado arredondado com o símbolo de reciclagem. */
@Composable
fun LogoPontoVerde(
    tamanho: Dp,
    corFundo: Color,
    corSimbolo: Color,
    cantos: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(tamanho)
            .background(corFundo, RoundedCornerShape(cantos)),
        contentAlignment = Alignment.Center
    ) {
        // ︎ pede a versão "texto" do símbolo, para respeitar a cor
        Text(
            text = "♻︎",
            color = corSimbolo,
            fontSize = (tamanho.value * 0.55f).sp
        )
    }
}

/** Fundo claro com o círculo verde decorativo no canto superior direito. */
@Composable
fun AuthFundo(conteudo: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CoresAuth.Fundo)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = CoresAuth.VerdeClaro,
                radius = 110.dp.toPx(),
                center = Offset(size.width, 0f)
            )
        }
        conteudo()
    }
}

/** Barra de topo: seta para voltar à esquerda e logótipo à direita. */
@Composable
fun AuthTopo(onVoltar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVoltar) {
            Icon(IconesAuth.Voltar, contentDescription = "Voltar", tint = CoresAuth.Texto)
        }
        LogoPontoVerde(
            tamanho = 48.dp,
            corFundo = CoresAuth.Verde,
            corSimbolo = Color.White,
            cantos = 14.dp
        )
    }
}

/** Etiqueta pequena em maiúsculas, título grande e subtítulo. */
@Composable
fun AuthCabecalho(etiqueta: String, titulo: String, subtitulo: String) {
    Column {
        Text(
            text = etiqueta,
            color = CoresAuth.Verde,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = titulo,
            color = CoresAuth.Texto,
            fontSize = 30.sp,
            lineHeight = 36.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitulo,
            color = CoresAuth.TextoSecundario,
            fontSize = 14.sp
        )
    }
}

/**
 * Campo de texto com o rótulo por cima, como nos mockups.
 * Se for password, tem o botão do olho para mostrar/ocultar.
 * "ajuda" aparece em cinzento; "erro" substitui-a a vermelho.
 */
@Composable
fun CampoAuth(
    rotulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    ePassword: Boolean = false,
    ajuda: String? = null,
    erro: String? = null
) {
    var visivel by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = rotulo,
            color = CoresAuth.Texto,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            placeholder = { Text(placeholder, color = CoresAuth.TextoSecundario.copy(alpha = 0.7f)) },
            singleLine = true,
            isError = erro != null,
            visualTransformation = if (ePassword && !visivel) PasswordVisualTransformation()
                                   else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (ePassword) KeyboardType.Password else tipoTeclado
            ),
            trailingIcon = if (ePassword) {
                {
                    IconButton(onClick = { visivel = !visivel }) {
                        Icon(
                            imageVector = if (visivel) IconesAuth.Ocultar else IconesAuth.Mostrar,
                            contentDescription = if (visivel) "Ocultar palavra-passe"
                                                 else "Mostrar palavra-passe",
                            tint = CoresAuth.TextoSecundario
                        )
                    }
                }
            } else null,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedBorderColor = CoresAuth.Verde,
                unfocusedBorderColor = CoresAuth.Borda,
                cursorColor = CoresAuth.Verde
            ),
            modifier = Modifier.fillMaxWidth()
        )
        val textoBaixo = erro ?: ajuda
        if (textoBaixo != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = textoBaixo,
                color = if (erro != null) CoresAuth.Erro else CoresAuth.TextoSecundario,
                fontSize = 11.sp
            )
        }
    }
}

/** Botão grande arredondado: verde cheio, ou só com contorno (preenchido = false). */
@Composable
fun BotaoAuth(
    texto: String,
    icone: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    preenchido: Boolean = true
) {
    val forma = RoundedCornerShape(50)
    val conteudo: @Composable () -> Unit = {
        Icon(icone, contentDescription = null, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        Text(texto, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }

    if (preenchido) {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = forma,
            colors = ButtonDefaults.buttonColors(
                containerColor = CoresAuth.Verde,
                contentColor = Color.White,
                disabledContainerColor = CoresAuth.Verde.copy(alpha = 0.4f),
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = if (enabled) 10.dp else 0.dp,
                    shape = forma,
                    ambientColor = CoresAuth.Verde,
                    spotColor = CoresAuth.Verde
                )
        ) { conteudo() }
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = forma,
            border = BorderStroke(1.dp, CoresAuth.Verde),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = CoresAuth.Verde
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) { conteudo() }
    }
}

/** Linha do fundo do ecrã: "Pergunta? Ação". */
@Composable
fun RodapeAuth(pergunta: String, acao: String, onAcao: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(pergunta, color = CoresAuth.TextoSecundario, fontSize = 13.sp)
        Text(
            text = acao,
            color = CoresAuth.Verde,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable(onClick = onAcao)
                .padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}
