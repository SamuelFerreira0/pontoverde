package com.example.pontoverde.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pontoverde.data.Ecoponto
import com.example.pontoverde.data.EcopontoTipo
import com.example.pontoverde.viewmodel.CamaraUiState
import com.example.pontoverde.viewmodel.CamaraViewModel

@Composable
fun CamaraScreen(
    modifier: Modifier = Modifier,
    viewModel: CamaraViewModel = viewModel()
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    if (!hasPermission) {
        PermissionDenied(
            onRequest = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            modifier = modifier
        )
        return
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (val current = state) {
        CamaraUiState.Pronto, CamaraUiState.ANalisar -> CameraContent(
            analysing = current == CamaraUiState.ANalisar,
            onImageCaptured = viewModel::onImageCaptured,
            onCaptureError = viewModel::onCaptureError,
            modifier = modifier
        )

        is CamaraUiState.Resultado -> ResultPanel(
            ecoponto = current.ecoponto,
            onNewPhoto = viewModel::onNewPhoto,
            modifier = modifier
        )

        CamaraUiState.NaoReconhecido -> MessagePanel(
            title = "Resíduo não reconhecido",
            text = "Aproxime-se do objeto, melhore a iluminação e tente outra vez.",
            onNewPhoto = viewModel::onNewPhoto,
            modifier = modifier
        )

        is CamaraUiState.Erro -> MessagePanel(
            title = "Ocorreu um erro",
            text = current.mensagem,
            onNewPhoto = viewModel::onNewPhoto,
            modifier = modifier
        )
    }
}

@Composable
private fun PermissionDenied(onRequest: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Precisamos de acesso à câmara para identificar o resíduo.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Button(onClick = onRequest, modifier = Modifier.padding(top = 16.dp)) {
            Text("Permitir câmara")
        }
    }
}

@Composable
private fun CameraContent(
    analysing: Boolean,
    onImageCaptured: (ImageProxy) -> Unit,
    onCaptureError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = remember { LifecycleCameraController(context) }

    DisposableEffect(lifecycleOwner) {
        controller.bindToLifecycle(lifecycleOwner)
        onDispose { controller.unbind() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { PreviewView(it).apply { this.controller = controller } },
            modifier = Modifier.fillMaxSize()
        )
        if (analysing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text(
                        text = "A analisar...",
                        color = Color.White,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        } else {
            FilledIconButton(
                onClick = {
                    controller.takePicture(
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) = onImageCaptured(image)
                            override fun onError(exception: ImageCaptureException) = onCaptureError()
                        }
                    )
                },
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .size(72.dp)
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Tirar fotografia")
            }
        }
    }
}

@Composable
private fun ResultPanel(ecoponto: Ecoponto, onNewPhoto: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(ecoponto.tipo.color(), CircleShape)
        )
        Text(
            text = ecoponto.residuo,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Ecoponto ${ecoponto.tipo.nome}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = ecoponto.instrucoes,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = onNewPhoto,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Nova fotografia")
        }
    }
}

@Composable
private fun MessagePanel(
    title: String,
    text: String,
    onNewPhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
        Button(
            onClick = onNewPhoto,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Nova fotografia")
        }
    }
}

private fun EcopontoTipo.color(): Color = when (this) {
    EcopontoTipo.AMARELO -> Color(0xFFF9A825)
    EcopontoTipo.AZUL -> Color(0xFF1565C0)
    EcopontoTipo.VERDE -> Color(0xFF2E7D32)
    EcopontoTipo.PILHAO -> Color(0xFFC62828)
    EcopontoTipo.ECOCENTRO -> Color(0xFF6D4C41)
}
