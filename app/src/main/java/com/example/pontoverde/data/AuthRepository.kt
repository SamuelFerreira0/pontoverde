package com.example.pontoverde.data

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Os erros de autenticação que a app sabe distinguir. */
enum class ErroAuth {
    CREDENCIAIS_INVALIDAS,
    EMAIL_INVALIDO,
    EMAIL_JA_EXISTE,
    PASSWORD_FRACA,
    SEM_INTERNET,
    DEMASIADAS_TENTATIVAS,
    DESCONHECIDO
}

/** Exceção da app: leva só o tipo de erro, sem nada do Firebase. */
class AuthException(val tipo: ErroAuth) : Exception(tipo.name)

/**
 * Camada de dados da autenticação.
 *
 * É o ÚNICO sítio da app que conhece o Firebase Auth. O ViewModel só vê
 * Result<Unit> e ErroAuth, por isso se um dia trocarmos o Firebase por outra
 * API, só este ficheiro muda.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    /** true se já existe sessão iniciada (o Firebase guarda-a no telemóvel). */
    val estaAutenticado: Boolean
        get() = auth.currentUser != null

    suspend fun entrar(email: String, password: String): Result<Unit> = executar {
        auth.signInWithEmailAndPassword(email, password).aguardar()
    }

    /** Cria a conta e guarda o nome no perfil do utilizador do Firebase. */
    suspend fun criarConta(nome: String, email: String, password: String): Result<Unit> = executar {
        val resultado = auth.createUserWithEmailAndPassword(email, password).aguardar()
        val perfil = UserProfileChangeRequest.Builder()
            .setDisplayName(nome)
            .build()
        resultado.user?.updateProfile(perfil)?.aguardar()
    }

    /** Envia um email do Firebase com um link para escolher nova palavra-passe. */
    suspend fun recuperarPassword(email: String): Result<Unit> = executar {
        auth.sendPasswordResetEmail(email).aguardar()
    }

    fun sair() {
        auth.signOut()
    }

    /**
     * Corre uma operação do Firebase e transforma o resultado num Result:
     * sucesso, ou falha com um AuthException que o ViewModel sabe ler.
     */
    private suspend fun executar(operacao: suspend () -> Unit): Result<Unit> =
        try {
            operacao()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e // o ecrã foi fechado: deixa a coroutine terminar normalmente
        } catch (e: Exception) {
            Result.failure(AuthException(traduzir(e)))
        }

    /** Converte as exceções do Firebase nos erros da app. */
    private fun traduzir(e: Exception): ErroAuth = when (e) {
        // A WeakPassword é um tipo de InvalidCredentials, por isso tem de vir primeiro
        is FirebaseAuthWeakPasswordException -> ErroAuth.PASSWORD_FRACA
        is FirebaseAuthUserCollisionException -> ErroAuth.EMAIL_JA_EXISTE
        is FirebaseAuthInvalidCredentialsException ->
            if (e.errorCode == "ERROR_INVALID_EMAIL") ErroAuth.EMAIL_INVALIDO
            else ErroAuth.CREDENCIAIS_INVALIDAS
        is FirebaseAuthInvalidUserException -> ErroAuth.CREDENCIAIS_INVALIDAS
        is FirebaseNetworkException -> ErroAuth.SEM_INTERNET
        is FirebaseTooManyRequestsException -> ErroAuth.DEMASIADAS_TENTATIVAS
        else -> ErroAuth.DESCONHECIDO
    }
}

/**
 * O Firebase devolve uma Task (com callbacks). Esta função "espera" pela Task
 * dentro de uma coroutine, para podermos escrever o código de cima para baixo.
 */
private suspend fun <T> Task<T>.aguardar(): T = suspendCancellableCoroutine { continuacao ->
    addOnCompleteListener { task ->
        val erro = task.exception
        when {
            erro != null -> continuacao.resumeWithException(erro)
            task.isCanceled -> continuacao.cancel()
            else -> continuacao.resume(task.result)
        }
    }
}
