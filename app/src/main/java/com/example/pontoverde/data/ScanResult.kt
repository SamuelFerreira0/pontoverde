package com.example.pontoverde.data

/** Rótulo devolvido pelo ML Kit (texto em inglês e confiança de 0 a 1). */
data class ScanLabel(val text: String, val confidence: Float)

/** O que a câmara entrega ao ecrã de resultado: a fotografia (guardada em cache) e os rótulos. */
data class ScanResult(val photoPath: String, val labels: List<ScanLabel>)
