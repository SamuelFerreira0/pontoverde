package com.example.pontoverde.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ecrã de Entrar ("Bem-vindo de volta").
 *
 * O ecrã não sabe nada de Firebase: só recolhe o email e a palavra-passe
 * e avisa quem o chamou através de callbacks. No Checkpoint 2, o onEntrar
 * passa a chamar o AuthViewModel.
 */
@Composable
fun LoginScreen(
    onVoltar: () -> Unit,
    onEntrar: (email: String, password: String) -> Unit,
    onEsqueciPassword: () -> Unit,
    onCriarConta: () -> Unit
) {
    // rememberSaveable guarda o texto mesmo se o ecrã rodar
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    AuthFundo {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Parte de cima, com scroll para ecrãs pequenos
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                AuthTopo(onVoltar = onVoltar)

                Spacer(Modifier.height(40.dp))

                AuthCabecalho(
                    etiqueta = "OLÁ NOVAMENTE",
                    titulo = "Bem-vindo de volta",
                    subtitulo = "Continua a construir hábitos mais sustentáveis."
                )

                Spacer(Modifier.height(32.dp))

                CampoAuth(
                    rotulo = "Email",
                    valor = email,
                    onValorChange = { email = it },
                    placeholder = "nome@exemplo.pt",
                    tipoTeclado = KeyboardType.Email
                )

                Spacer(Modifier.height(20.dp))

                CampoAuth(
                    rotulo = "Palavra-passe",
                    valor = password,
                    onValorChange = { password = it },
                    placeholder = "Introduz a tua palavra-passe",
                    ePassword = true
                )

                Text(
                    text = "Esqueci-me da palavra-passe",
                    color = CoresAuth.Verde,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 6.dp)
                        .clickable(onClick = onEsqueciPassword)
                        .padding(4.dp)
                )

                Spacer(Modifier.height(24.dp))

                BotaoAuth(
                    texto = "Entrar",
                    icone = IconesAuth.Entrar,
                    onClick = { onEntrar(email.trim(), password) },
                    enabled = email.isNotBlank() && password.isNotBlank()
                )

                Spacer(Modifier.height(16.dp))
            }

            RodapeAuth(
                pergunta = "Ainda não tens conta?",
                acao = "Criar conta",
                onAcao = onCriarConta
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onVoltar = {},
        onEntrar = { _, _ -> },
        onEsqueciPassword = {},
        onCriarConta = {}
    )
}
