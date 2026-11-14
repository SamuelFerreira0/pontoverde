package com.example.pontoverde.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pontoverde.data.Ecoponto
import com.example.pontoverde.data.EcopontoRepository
import com.example.pontoverde.data.TipoEcoponto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Ponto de teste, no centro dos ecopontos. No CP4 passa a ser a localização real.
private const val LAT_TESTE = 41.6990
private const val LON_TESTE = -8.8320

/** Tudo o que o ecrã do mapa precisa de mostrar. */
data class MapUiState(
    val ecopontos: List<Ecoponto> = emptyList(),
    val filtro: TipoEcoponto? = TipoEcoponto.AMARELO,
    val selecionadoId: String? = null,
    val latitude: Double = LAT_TESTE,
    val longitude: Double = LON_TESTE,
    val erro: Boolean = false
) {
    /** Ecopontos que passam no filtro escolhido. */
    val visiveis: List<Ecoponto>
        get() {
            val f = filtro ?: return ecopontos
            return ecopontos.filter { f in it.tipos }
        }

    /** Só os tipos que têm ecopontos, para não mostrar filtros vazios. */
    val tiposDisponiveis: List<TipoEcoponto>
        get() = ecopontos.flatMap { it.tipos }.distinct().sortedBy { it.ordinal }

    /** O ecoponto escolhido; se não houver nenhum, o mais próximo. */
    val selecionado: Ecoponto?
        get() = visiveis.firstOrNull { it.id == selecionadoId }
            ?: visiveis.minByOrNull { it.distanciaMetros(latitude, longitude) }
}

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = EcopontoRepository(application)

    private val _estado = MutableStateFlow(MapUiState())
    val estado: StateFlow<MapUiState> = _estado.asStateFlow()

    init {
        carregarEcopontos()
    }

    private fun carregarEcopontos() {
        try {
            val lista = repositorio.obterTodos()
            _estado.update { it.copy(ecopontos = lista) }
        } catch (e: Exception) {
            _estado.update { it.copy(erro = true) }
        }
    }

    /** Toca num filtro: liga-o; se já estava ligado, desliga-o. */
    fun alterarFiltro(tipo: TipoEcoponto) {
        _estado.update {
            it.copy(
                filtro = if (it.filtro == tipo) null else tipo,
                selecionadoId = null
            )
        }
    }

    fun selecionar(id: String) {
        _estado.update { it.copy(selecionadoId = id) }
    }
}