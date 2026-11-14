package com.example.pontoverde.data

data class PassoDeposicao(val titulo: String, val descricao: String)

data class WasteInfo(
    val nome: String,
    val ecoponto: TipoEcoponto,
    val passos: List<PassoDeposicao>,
    val confidence: Float,
)