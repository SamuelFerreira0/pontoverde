package com.example.pontoverde.viewmodel

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pontoverde.data.ScanLabel
import com.example.pontoverde.data.ScanResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

sealed interface CameraUiState {
    data object Ready : CameraUiState
    data class Identifying(val photo: Bitmap?) : CameraUiState
    data class Error(val message: String) : CameraUiState
}

class CameraViewModel : ViewModel() {

    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    private val _uiState = MutableStateFlow<CameraUiState>(CameraUiState.Ready)
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    // Evento de uma só vez: o ecrã recebe o resultado e navega para o resultado.
    private val _scans = Channel<ScanResult>(Channel.BUFFERED)
    val scans = _scans.receiveAsFlow()

    fun onImageCaptured(image: ImageProxy, cacheDir: File) {
        if (_uiState.value != CameraUiState.Ready) {
            image.close()
            return
        }
        _uiState.value = CameraUiState.Identifying(null)

        viewModelScope.launch {
            val photo = withContext(Dispatchers.Default) {
                try {
                    image.toBitmap()
                        .rotate(image.imageInfo.rotationDegrees)
                        .scaleDown(MAX_SIDE)
                } catch (e: Exception) {
                    null
                } finally {
                    image.close()
                }
            }
            if (photo == null) {
                fail("Não foi possível processar a fotografia.")
                return@launch
            }
            _uiState.value = CameraUiState.Identifying(photo)

            val photoPath = withContext(Dispatchers.IO) { saveJpeg(photo, cacheDir) }
            if (photoPath == null) {
                fail("Não foi possível guardar a fotografia.")
                return@launch
            }

            labeler.process(InputImage.fromBitmap(photo, 0))
                .addOnSuccessListener { labels ->
                    _scans.trySend(
                        ScanResult(photoPath, labels.map { ScanLabel(it.text, it.confidence) })
                    )
                    _uiState.value = CameraUiState.Ready
                }
                .addOnFailureListener {
                    fail("Não foi possível identificar o objeto. Tenta outra vez.")
                }
        }
    }

    fun onCaptureError() = fail("Não foi possível tirar a fotografia.")

    fun dismissError() {
        _uiState.value = CameraUiState.Ready
    }

    private fun fail(message: String) {
        _uiState.value = CameraUiState.Error(message)
    }

    override fun onCleared() {
        labeler.close()
    }

    private companion object {
        const val MAX_SIDE = 1280
    }
}

private fun Bitmap.rotate(degrees: Int): Bitmap {
    if (degrees == 0) return this
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

// Reduz fotografias grandes: poupa memória e acelera o ML Kit.
private fun Bitmap.scaleDown(maxSide: Int): Bitmap {
    val largest = maxOf(width, height)
    if (largest <= maxSide) return this
    val ratio = maxSide.toFloat() / largest
    return Bitmap.createScaledBitmap(this, (width * ratio).toInt(), (height * ratio).toInt(), true)
}

private fun saveJpeg(bitmap: Bitmap, dir: File): String? = try {
    val file = File(dir, "scan_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
    file.absolutePath
} catch (e: IOException) {
    null
}
