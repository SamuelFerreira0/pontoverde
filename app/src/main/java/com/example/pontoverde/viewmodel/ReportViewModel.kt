package com.example.pontoverde.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pontoverde.ui.report.MAX_DESCRIPTION
import com.example.pontoverde.ui.report.ProblemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Tudo o que o ecrã de sinalização mostra, num único objeto.
data class ReportUiState(
    val locationName: String = "A tua localização",
    val city: String = "Lisboa",
    val selected: ProblemType = ProblemType.Full,
    val description: String = "",
)

class ReportViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    // Ação do ecrã: o utilizador escolheu um tipo de problema.
    fun onProblemSelected(type: ProblemType) {
        _uiState.update { it.copy(selected = type) }
    }

    // Ação do ecrã: o utilizador escreveu na descrição (máximo de caracteres).
    fun onDescriptionChange(text: String) {
        if (text.length <= MAX_DESCRIPTION) {
            _uiState.update { it.copy(description = text) }
        }
    }
}