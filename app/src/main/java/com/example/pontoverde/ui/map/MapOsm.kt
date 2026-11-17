package com.example.pontoverde.ui.map

import android.graphics.drawable.GradientDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pontoverde.data.Ecoponto
import com.example.pontoverde.data.TipoEcoponto
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * Imagens do mapa servidas pela CARTO, com dados do OpenStreetMap.
 * Os servidores oficiais do OpenStreetMap (voluntários) bloqueiam apps, por isso usamos este.
 */
private val MapaCarto = XYTileSource(
    "CartoVoyager", 0, 20, 256, ".png",
    arrayOf(
        "https://a.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://b.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://c.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://d.basemaps.cartocdn.com/rastertiles/voyager/"
    ),
    "© OpenStreetMap contributors © CARTO"
)

/**
 * O mapa real (dados do OpenStreetMap, desenhado pelo osmdroid).
 * Tal como o resto do ecrã, só desenha o que recebe e avisa quando tocas num pin.
 */
@Composable
fun MapaOsm(
    ecopontos: List<Ecoponto>,
    filtro: TipoEcoponto?,
    selecionadoId: String?,
    latitude: Double,
    longitude: Double,
    pedidoCentrar: Int,
    onPinClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // O MapView (vista do osmdroid) é criado uma só vez e reaproveitado
    val mapView = remember {
        MapView(context).apply {
            setTileSource(MapaCarto)                  // imagens da CARTO (dados do OpenStreetMap)
            setMultiTouchControls(true)               // zoom com dois dedos
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(16.0)
        }
    }

    // O mapa pausa quando sais do ecrã e liberta memória quando ele fecha
    DisposableEffect(lifecycle) {
        val observador = LifecycleEventObserver { _, evento ->
            when (evento) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycle.addObserver(observador)
        onDispose {
            lifecycle.removeObserver(observador)
            mapView.onDetach()
        }
    }

    // Centra no utilizador ao abrir e sempre que se carrega no botão de centrar
    LaunchedEffect(latitude, longitude, pedidoCentrar) {
        mapView.controller.animateTo(GeoPoint(latitude, longitude))
    }

    // Quando escolhes outro ecoponto, o mapa desliza até ele
    LaunchedEffect(selecionadoId) {
        ecopontos.find { it.id == selecionadoId }?.let {
            mapView.controller.animateTo(GeoPoint(it.latitude, it.longitude))
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        // Corre sempre que o estado muda (filtro, seleção...): apaga os pins e volta a pô-los
        update = { mapa ->
            mapa.overlays.clear()

            ecopontos.forEach { eco ->
                val tipo = filtro ?: eco.tipos.first()
                val pin = Marker(mapa).apply {
                    position = GeoPoint(eco.latitude, eco.longitude)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    icon = icon?.mutate()?.also { it.setTint(tipo.cor.toArgb()) }
                    alpha = if (eco.id == selecionadoId) 1f else 0.7f   // o escolhido destaca-se
                    setOnMarkerClickListener { _, _ ->
                        onPinClick(eco.id)
                        true   // true = não abre o balão de texto do osmdroid
                    }
                }
                mapa.overlays.add(pin)
            }

            // Posição do utilizador: bolinha azul com borda branca (ponto de teste até ao CP4)
            val densidade = context.resources.displayMetrics.density
            val eu = Marker(mapa).apply {
                position = GeoPoint(latitude, longitude)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                icon = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(0xFF1976D2.toInt())
                    setStroke((4 * densidade).toInt(), android.graphics.Color.WHITE)
                    setSize((22 * densidade).toInt(), (22 * densidade).toInt())
                }
                setOnMarkerClickListener { _, _ -> true }
            }
            mapa.overlays.add(eu)

            mapa.invalidate()   // redesenha o mapa
        }
    )
}