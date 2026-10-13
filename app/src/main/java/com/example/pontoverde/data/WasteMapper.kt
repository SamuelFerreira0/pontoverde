package com.example.pontoverde.data

enum class EcopontoTipo(val nome: String) {
    AMARELO("Amarelo"),
    AZUL("Azul"),
    VERDE("Verde"),
    PILHAO("Pilhão"),
    ECOCENTRO("Ecocentro")
}

data class Ecoponto(
    val tipo: EcopontoTipo,
    val residuo: String,
    val instrucoes: String
)

data class Label(val text: String, val confidence: Float)

data class Resultado(val ecoponto: Ecoponto, val confidence: Float)

object WasteMapper {
    const val CONFIDENCE_THRESHOLD = 0.6f

    private val plastico = Ecoponto(
        EcopontoTipo.AMARELO, "Embalagem de plástico",
        "Esvazie a embalagem, aperte-a e deposite-a no ecoponto amarelo."
    )
    private val metal = Ecoponto(
        EcopontoTipo.AMARELO, "Embalagem de metal",
        "Esvazie a lata e deposite-a no ecoponto amarelo."
    )
    private val embalagem = Ecoponto(
        EcopontoTipo.AMARELO, "Embalagem",
        "Esvazie a embalagem e deposite-a no ecoponto amarelo."
    )
    private val papel = Ecoponto(
        EcopontoTipo.AZUL, "Papel",
        "Deposite no ecoponto azul, sem restos de comida ou gordura."
    )
    private val cartao = Ecoponto(
        EcopontoTipo.AZUL, "Cartão",
        "Dobre a caixa para ocupar menos espaço e deposite-a no ecoponto azul."
    )
    private val vidro = Ecoponto(
        EcopontoTipo.VERDE, "Embalagem de vidro",
        "Esvazie e deposite no ecoponto verde, sem tampa. Loiça, espelhos e cristal não entram."
    )
    private val pilha = Ecoponto(
        EcopontoTipo.PILHAO, "Pilha ou bateria",
        "Deposite no pilhão. Nunca coloque pilhas no lixo comum."
    )
    private val lampada = Ecoponto(
        EcopontoTipo.ECOCENTRO, "Lâmpada",
        "Leve a um ecocentro ou entregue numa loja que venda lâmpadas. Não vai ao vidrão."
    )

    // Chaves em minúsculas; os rótulos do ML Kit vêm em inglês.
    private val table: Map<String, Ecoponto> = mapOf(
        "bottle" to plastico,
        "plastic bottle" to plastico,
        "plastic" to plastico,
        "plastic bag" to plastico,
        "packaging and labeling" to embalagem,
        "packaging" to embalagem,
        "can" to metal,
        "drink can" to metal,
        "tin" to metal,
        "tin can" to metal,
        "aluminium" to metal,
        "aluminum" to metal,
        "cardboard" to cartao,
        "carton" to cartao,
        "box" to cartao,
        "paper" to papel,
        "paper product" to papel,
        "newspaper" to papel,
        "magazine" to papel,
        "glass" to vidro,
        "glass bottle" to vidro,
        "wine bottle" to vidro,
        "jar" to vidro,
        "battery" to pilha,
        "light bulb" to lampada,
        "lightbulb" to lampada,
        "lamp" to lampada
    )

    /** Devolve o melhor rótulo reconhecido acima do limiar, ou null se não reconhecido. */
    fun map(labels: List<Label>): Resultado? =
        labels
            .filter { it.confidence >= CONFIDENCE_THRESHOLD }
            .sortedByDescending { it.confidence }
            .firstNotNullOfOrNull { label ->
                table[label.text.trim().lowercase()]?.let { Resultado(it, label.confidence) }
            }
}
