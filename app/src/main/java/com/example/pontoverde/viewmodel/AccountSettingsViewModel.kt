package com.example.pontoverde.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Tudo o que o ecrã de Definições da conta mostra. Os dados são fictícios por agora. */
data class AccountSettingsUiState(
    val nome: String = "Ana Martins",          // o que está escrito no campo
    val nomeGuardado: String = "Ana Martins",  // o último nome guardado
    val email: String = "ana.martins@email.com",
    val guardado: Boolean = false,             // mostra a mensagem "Alterações guardadas"
)

class AccountSettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AccountSettingsUiState())
    val uiState: StateFlow<AccountSettingsUiState> = _uiState.asStateFlow()

    /** O utilizador escreveu no campo do nome. */
    fun onNameChange(texto: String) {
        _uiState.update { it.copy(nome = texto, guardado = false) }
    }

    /** O utilizador carregou em "Guardar alterações". Um nome vazio mantém o anterior. */
    fun onSave() {
        _uiState.update {
            val limpo = it.nome.trim().ifEmpty { it.nomeGuardado }
            it.copy(nome = limpo, nomeGuardado = limpo, guardado = true)
        }
    }
}