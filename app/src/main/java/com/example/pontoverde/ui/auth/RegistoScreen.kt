package com.example.pontoverde.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Ecrã de Criar conta.
 *
 * Faz validações simples no próprio ecrã (formato do email, palavra-passe
 * com pelo menos 6 caracteres, palavras-passe iguais). O Firebase Auth exige
 * no mínimo 6 caracteres, por isso validamos isso antes de enviar.
 */
@Composable
fun RegistoScreen(
    onVoltar: () -> Unit,
    onCriarConta: (nome: String, email: String, password: String) -> Unit,
    onEntrar: () -> Unit
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }

    // Validações: só mostram erro depois de o utilizador escrever algo
    val emailInvalido = email.isNotEmpty() &&
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val passwordCurta = password.isNotEmpty() && password.length < 6
    val passwordsDiferentes = confirmar.isNotEmpty() && confirmar != password

    val formularioValido = nome.isNotBlank() &&
        email.isNotBlank() && !emailInvalido &&
        password.length >= 6 &&
        confirmar == password

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

                Spacer(Modifier.height(24.dp))

                AuthCabecalho(
                    etiqueta = "OLÁ NOVAMENTE",
                    titulo = "Cria a tua conta",
                    subtitulo = "Continua a construir hábitos mais sustentáveis."
                )

                Spacer(Modifier.height(24.dp))

                CampoAuth(
                    rotulo = "Nome",
                    valor = nome,
                    onValorChange = { nome = it },
                    placeholder = "O teu nome"
                )

                Spacer(Modifier.height(16.dp))

                CampoAuth(
                    rotulo = "Email",
                    valor = email,
                    onValorChange = { email = it },
                    placeholder = "nome@exemplo.pt",
                    tipoTeclado = KeyboardType.Email,
                    erro = if (emailInvalido) "Email inválido" else null
                )

                Spacer(Modifier.height(16.dp))

                CampoAuth(
                    rotulo = "Palavra-passe",
                    valor = password,
                    onValorChange = { password = it },
                    placeholder = "Cria uma palavra-passe",
                    ePassword = true,
                    ajuda = "Mínimo de 6 caracteres",
                    erro = if (passwordCurta) "Mínimo de 6 caracteres" else null
                )

                Spacer(Modifier.height(16.dp))

                CampoAuth(
                    rotulo = "Confirmar palavra-passe",
                    valor = confirmar,
                    onValorChange = { confirmar = it },
                    placeholder = "Repete a palavra-passe",
                    ePassword = true,
                    erro = if (passwordsDiferentes) "As palavras-passe não coincidem" else null
                )

                Spacer(Modifier.height(20.dp))

                BotaoAuth(
                    texto = "Criar conta",
                    icone = IconesAuth.CriarConta,
                    onClick = { onCriarConta(nome.trim(), email.trim(), password) },
                    enabled = formularioValido
                )

                Spacer(Modifier.height(16.dp))
            }

            RodapeAuth(
                pergunta = "Já tens conta?",
                acao = "Entrar",
                onAcao = onEntrar
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RegistoScreenPreview() {
    RegistoScreen(
        onVoltar = {},
        onCriarConta = { _, _, _ -> },
        onEntrar = {}
    )
}
