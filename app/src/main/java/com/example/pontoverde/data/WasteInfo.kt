package com.example.pontoverde.data

import com.example.pontoverde.ui.map.TipoEcoponto

data class PassoDeposicao(val titulo: String, val descricao: String)

data class WasteInfo(
    val nome: String,
    val ecoponto: TipoEcoponto,
    val passos: List<PassoDeposicao>,
    val confidence: Float,
)
