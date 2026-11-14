package com.example.pontoverde.data

import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

enum class TipoEcoponto { AMARELO, AZUL, VERDE, PILHAO, ECOCENTRO, VOLTA }

data class Ecoponto(
    val id: String,
    val nome: String?,          // só as máquinas Volta têm nome
    val latitude: Double,
    val longitude: Double,
    val tipos: List<TipoEcoponto>
) {
    /** Distância em linha reta até um ponto, em metros (fórmula de Haversine). */
    fun distanciaMetros(lat: Double, lon: Double): Double {
        val raioTerra = 6_371_000.0
        val dLat = (lat - latitude).toRad()
        val dLon = (lon - longitude).toRad()
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(latitude.toRad()) * cos(lat.toRad()) * sin(dLon / 2) * sin(dLon / 2)
        return 2 * raioTerra * asin(sqrt(a))
    }

    /** Estimativa a pé: cerca de 80 metros por minuto. */
    fun minutosAPe(lat: Double, lon: Double): Int =
        (distanciaMetros(lat, lon) / 80.0).roundToInt().coerceAtLeast(1)
}

private fun Double.toRad() = this * PI / 180.0