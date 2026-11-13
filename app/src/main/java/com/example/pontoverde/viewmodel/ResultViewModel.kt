package com.example.pontoverde.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pontoverde.data.PassoDeposicao
import com.example.pontoverde.data.ScanLabel
import com.example.pontoverde.data.ScanResult
import com.example.pontoverde.data.WasteInfo
import com.example.pontoverde.ui.map.TipoEcoponto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface ResultUiState {
    data object Empty : ResultUiState
    data class Recognized(
        val photoPath: String,
        val waste: WasteInfo,
        val saved: Boolean = false,
    ) : ResultUiState
    data class NotRecognized(val photoPath: String) : ResultUiState
}

class ResultViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ResultUiState>(ResultUiState.Empty)
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    fun onScan(scan: ScanResult) {
        val waste = provisionalMap(scan.labels)
        _uiState.value = if (waste == null) {
            ResultUiState.NotRecognized(scan.photoPath)
        } else {
            ResultUiState.Recognized(scan.photoPath, waste)
        }
    }

    // Por agora só muda o ecrã; a gravação no Room entra com o histórico.
    fun save() {
        _uiState.update { state ->
            if (state is ResultUiState.Recognized) state.copy(saved = true) else state
        }
    }
}

private const val MIN_CONFIDENCE = 0.6f

private val PASSOS_AMARELO = listOf(
    PassoDeposicao("Esvazia", "Retira todo o conteúdo"),
    PassoDeposicao("Espalma", "Ocupa menos espaço"),
    PassoDeposicao("Deposita", "Sem tampa, no amarelo"),
)

private val PASSOS_AZUL = listOf(
    PassoDeposicao("Desdobra", "Abre a caixa para ocupar menos espaço"),
    PassoDeposicao("Remove", "Tira fitas-cola e agrafos"),
    PassoDeposicao("Deposita", "No ecoponto azul"),
)

// Provisório: troca-se pelo LabelMapper do Tiago quando entrar na develop.
private fun provisionalMap(labels: List<ScanLabel>): WasteInfo? {
    for (label in labels.sortedByDescending { it.confidence }) {
        if (label.confidence < MIN_CONFIDENCE) break
        when (label.text.lowercase()) {
            "bottle" -> return WasteInfo(
                "Garrafa de plástico", TipoEcoponto.AMARELO, PASSOS_AMARELO, label.confidence
            )
            "cardboard", "paper" -> return WasteInfo(
                "Cartão", TipoEcoponto.AZUL, PASSOS_AZUL, label.confidence
            )
        }
    }
    return null
}
