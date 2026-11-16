package com.example.pontoverde.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pontoverde.data.AuthException
import com.example.pontoverde.data.AuthRepository
import com.example.pontoverde.data.ErroAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/** Tudo o que os ecrãs Entrar e Criar conta precisam de mostrar. */
data class AuthUiState(
    val aCarregar: Boolean = false,
    val erro: String? = null,   // mensagem a vermelho
    val aviso: String? = null   // mensagem a verde (ex.: email de recuperação enviado)
)

/** Acontecimentos de uma só vez, que o ecrã não deve repetir ao rodar. */
sealed interface AuthEvento {
    data object Autenticado : AuthEvento
}

/**
 * ViewModel da autenticação, usado pelos ecrãs Entrar e Criar conta.
 *
 * Recebe as ações do ecrã (entrar, criarConta...), pede ao AuthRepository
 * e expõe o resultado em uiState. Como sobrevive à rotação do ecrã, um pedido
 * a meio não se perde se o utilizador rodar o telemóvel.
 */
class AuthViewModel(
    private val repositorio: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Mesmo padrão do CameraViewModel: a navegação é um evento, não um estado.
    private val _eventos = Channel<AuthEvento>(Channel.BUFFERED)
    val eventos = _eventos.receiveAsFlow()

    fun entrar(email: String, password: String) = autenticar {
        repositorio.entrar(email, password)
    }

    fun criarConta(nome: String, email: String, password: String) = autenticar {
        repositorio.criarConta(nome, email, password)
    }

    fun recuperarPassword(email: String) {
        if (email.isBlank()) {
            _uiState.value = AuthUiState(erro = "Escreve o teu email para recuperares a palavra-passe.")
            return
        }
        if (_uiState.value.aCarregar) return

        viewModelScope.launch {
            _uiState.value = AuthUiState(aCarregar = true)
            repositorio.recuperarPassword(email)
                .onSuccess {
                    _uiState.value = AuthUiState(aviso = "Enviámos um email para $email com um link para mudares a palavra-passe.")
                }
                .onFailure { e -> _uiState.value = AuthUiState(erro = mensagem(e)) }
        }
    }

    /** Entrar e Criar conta funcionam da mesma forma; muda só o pedido ao repositório. */
    private fun autenticar(pedido: suspend () -> Result<Unit>) {
        if (_uiState.value.aCarregar) return // evita pedidos repetidos com dois toques no botão

        viewModelScope.launch {
            _uiState.value = AuthUiState(aCarregar = true)
            pedido()
                .onSuccess {
                    _uiState.value = AuthUiState()
                    _eventos.trySend(AuthEvento.Autenticado)
                }
                .onFailure { e -> _uiState.value = AuthUiState(erro = mensagem(e)) }
        }
    }

    /** Transforma o erro numa frase para o utilizador. */
    private fun mensagem(e: Throwable): String {
        val tipo = (e as? AuthException)?.tipo ?: ErroAuth.DESCONHECIDO
        return when (tipo) {
            ErroAuth.CREDENCIAIS_INVALIDAS -> "Email ou palavra-passe incorretos."
            ErroAuth.EMAIL_INVALIDO -> "O email não é válido."
            ErroAuth.EMAIL_JA_EXISTE -> "Já existe uma conta com este email."
            ErroAuth.PASSWORD_FRACA -> "A palavra-passe é demasiado fraca. Usa pelo menos 6 caracteres."
            ErroAuth.SEM_INTERNET -> "Sem ligação à internet. Tenta outra vez."
            ErroAuth.DEMASIADAS_TENTATIVAS -> "Demasiadas tentativas. Espera um pouco e tenta outra vez."
            ErroAuth.DESCONHECIDO -> "Algo correu mal. Tenta outra vez."
        }
    }
}
