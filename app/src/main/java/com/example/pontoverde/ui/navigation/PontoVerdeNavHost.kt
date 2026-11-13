package com.example.pontoverde.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pontoverde.ui.auth.BoasVindasScreen
import com.example.pontoverde.ui.auth.LoginScreen
import com.example.pontoverde.ui.auth.RegistoScreen
import com.example.pontoverde.ui.camera.CameraScreen
import com.example.pontoverde.ui.map.MapScreen
import com.example.pontoverde.ui.permissions.PermissionsScreen
import com.example.pontoverde.ui.profile.ProfileScreen
import com.example.pontoverde.ui.report.ReportScreen
import com.example.pontoverde.ui.report.ReportSentScreen
import com.example.pontoverde.ui.settings.AccountSettingsScreen

@Composable
fun PontoVerdeNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route
    val temBarra = rotaAtual in Rotas.barraInferior

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0), // cada ecrã trata das suas margens
        bottomBar = {
            if (temBarra) {
                BarraInferior(rotaAtual) { destino -> navController.irParaSeparador(destino) }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.BOAS_VINDAS,
            modifier = Modifier
                .padding(padding)
                .then(if (temBarra) Modifier else Modifier.navigationBarsPadding())
        ) {
            // --- Fluxo de entrada ---
            composable(Rotas.BOAS_VINDAS) {
                BoasVindasScreen(
                    onCriarConta = { navController.navigate(Rotas.REGISTO) },
                    onEntrar = { navController.navigate(Rotas.LOGIN) }
                )
            }
            composable(Rotas.LOGIN) {
                LoginScreen(
                    onVoltar = { navController.popBackStack() },
                    onEntrar = { _, _ -> navController.irParaPermissoes() },
                    onEsqueciPassword = { /* TODO: ecrã de recuperar palavra-passe */ },
                    onCriarConta = {
                        navController.navigate(Rotas.REGISTO) {
                            popUpTo(Rotas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rotas.REGISTO) {
                RegistoScreen(
                    onVoltar = { navController.popBackStack() },
                    onCriarConta = { _, _, _ -> navController.irParaPermissoes() },
                    onEntrar = {
                        navController.navigate(Rotas.LOGIN) {
                            popUpTo(Rotas.REGISTO) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rotas.PERMISSOES) {
                PermissionsScreen(
                    onPermitir = {
                        navController.navigate(Rotas.CAMARA) {
                            popUpTo(Rotas.PERMISSOES) { inclusive = true }
                        }
                    }
                )
            }

            // --- Ecrãs da barra de baixo ---
            composable(Rotas.CAMARA) {
                CameraScreen(
                    onScanned = { navController.navigate(Rotas.RESULTADO) },
                    onPermissionNotNow = { navController.irParaSeparador(Rotas.MAPA) }
                )
            }
            composable(Rotas.MAPA) {
                MapScreen(onSinalizar = { navController.irParaSeparador(Rotas.REPORTAR) })
            }
            composable(Rotas.PERFIL) {
                // TODO: ligar às Definições quando o ProfileScreen tiver o parâmetro onSettings
                // ProfileScreen(onSettings = { navController.navigate(Rotas.DEFINICOES) })
                ProfileScreen()
            }
            composable(Rotas.REPORTAR) {
                ReportScreen(
                    onSubmit = { _, _ -> navController.navigate(Rotas.REPORT_ENVIADO) }
                )
            }

            // --- Fora da barra ---
            composable(Rotas.RESULTADO) { PlaceholderEcra("Resultado") }
            composable(Rotas.REPORT_ENVIADO) {
                ReportSentScreen(
                    onBackToMap = { navController.irParaSeparador(Rotas.MAPA) },
                    onGoToCamera = { navController.irParaSeparador(Rotas.CAMARA) }
                )
            }
            composable(Rotas.DEFINICOES) {
                AccountSettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

/** Troca de separador sem empilhar ecrãs repetidos e guardando o estado de cada um. */
private fun NavHostController.irParaSeparador(rota: String) {
    navigate(rota) {
        popUpTo(Rotas.CAMARA) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Depois do login/registo: vai às permissões e apaga o fluxo de entrada da pilha. */
private fun NavHostController.irParaPermissoes() {
    navigate(Rotas.PERMISSOES) {
        popUpTo(Rotas.BOAS_VINDAS) { inclusive = true }
    }
}