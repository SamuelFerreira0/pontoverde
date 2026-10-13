package com.example.pontoverde.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WasteMapperTest {
    @Test
    fun mapsKnownLabelCaseInsensitive() {
        val result = WasteMapper.map(listOf(Label("Cardboard", 0.8f)))
        assertEquals(EcopontoTipo.AZUL, result?.ecoponto?.tipo)
    }

    @Test
    fun ignoresLabelsBelowThreshold() {
        assertNull(WasteMapper.map(listOf(Label("Bottle", 0.59f))))
    }

    @Test
    fun returnsNullWhenNoMatch() {
        assertNull(WasteMapper.map(listOf(Label("Cat", 0.95f))))
    }

    @Test
    fun skipsUnmappedLabelAndUsesNextMatch() {
        val result = WasteMapper.map(listOf(Label("Table", 0.9f), Label("Glass", 0.7f)))
        assertEquals(EcopontoTipo.VERDE, result?.ecoponto?.tipo)
    }
}
