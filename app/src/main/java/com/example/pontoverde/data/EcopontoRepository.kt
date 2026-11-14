package com.example.pontoverde.data

import android.content.Context
import org.json.JSONObject

/** Lê os ecopontos do ficheiro assets/ecopontos.json. */
class EcopontoRepository(context: Context) {

    private val appContext = context.applicationContext

    fun obterTodos(): List<Ecoponto> {
        val texto = appContext.assets.open("ecopontos.json")
            .bufferedReader()
            .use { it.readText() }
        val lista = JSONObject(texto).getJSONArray("ecopontos")

        return List(lista.length()) { i ->
            val item = lista.getJSONObject(i)
            val tipos = item.getJSONArray("tipos")
            Ecoponto(
                id = item.getString("id"),
                nome = item.optString("nome").ifEmpty { null },
                latitude = item.getDouble("lat"),
                longitude = item.getDouble("lon"),
                tipos = List(tipos.length()) { t -> TipoEcoponto.valueOf(tipos.getString(t)) }
            )
        }
    }
}