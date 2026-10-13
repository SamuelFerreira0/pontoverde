package com.example.pontoverde.viewmodel

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pontoverde.data.Ecoponto
import com.example.pontoverde.data.Label
import com.example.pontoverde.data.WasteMapper
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CamaraUiState {
    data object Pronto : CamaraUiState
    data object ANalisar : CamaraUiState
    data class Resultado(val ecoponto: Ecoponto) : CamaraUiState
    data object NaoReconhecido : CamaraUiState
    data class Erro(val mensagem: String) : CamaraUiState
}

class CamaraViewModel : ViewModel() {
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    private val _uiState = MutableStateFlow<CamaraUiState>(CamaraUiState.Pronto)
    val uiState: StateFlow<CamaraUiState> = _uiState.asStateFlow()

    fun onImageCaptured(image: ImageProxy) {
        if (_uiState.value != CamaraUiState.Pronto) {
            image.close()
            return
        }
        _uiState.value = CamaraUiState.ANalisar
        viewModelScope.launch(Dispatchers.Default) {
            val input = try {
                InputImage.fromBitmap(image.toBitmap(), image.imageInfo.rotationDegrees)
            } catch (e: Exception) {
                _uiState.value = CamaraUiState.Erro("Não foi possível processar a fotografia.")
                return@launch
            } finally {
                image.close()
            }
            labeler.process(input)
                .addOnSuccessListener { labels ->
                    val result = WasteMapper.map(labels.map { Label(it.text, it.confidence) })
                    _uiState.value = result?.let { CamaraUiState.Resultado(it.ecoponto) }
                        ?: CamaraUiState.NaoReconhecido
                }
                .addOnFailureListener {
                    _uiState.value = CamaraUiState.Erro("Falha ao reconhecer o resíduo. Tente novamente.")
                }
        }
    }

    fun onCaptureError() {
        _uiState.value = CamaraUiState.Erro("Não foi possível tirar a fotografia.")
    }

    fun onNewPhoto() {
        _uiState.value = CamaraUiState.Pronto
    }

    override fun onCleared() {
        labeler.close()
    }
}
