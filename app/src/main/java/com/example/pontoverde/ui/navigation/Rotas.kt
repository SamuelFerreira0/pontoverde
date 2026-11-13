package com.example.pontoverde.ui.navigation

object Rotas {
    // Fluxo de entrada
    const val BOAS_VINDAS = "boas_vindas"
    const val LOGIN = "login"
    const val REGISTO = "registo"
    const val PERMISSOES = "permissoes"

    // Ecrãs da barra de baixo
    const val CAMARA = "camara"
    const val MAPA = "mapa"
    const val PERFIL = "perfil"
    const val REPORTAR = "reportar"

    // Ecrãs fora da barra
    const val RESULTADO = "resultado"
    const val REPORT_ENVIADO = "report_enviado"
    const val DEFINICOES = "definicoes"

    /** Rotas que mostram a barra de baixo. */
    val barraInferior = listOf(CAMARA, MAPA, PERFIL, REPORTAR)
}