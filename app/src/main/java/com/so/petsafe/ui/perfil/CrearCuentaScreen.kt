package com.so.petsafe.ui.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.SesionFalsa
import com.so.petsafe.domain.Validaciones
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun CrearCuentaScreen(onSesionIniciada: () -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.crear_cuenta), onVolver) }, containerColor = GrisSuave) { p ->
        CrearCuentaContenido(
            onCrear = { SesionFalsa.haySesion = true; onSesionIniciada() },
            modifier = Modifier
                .padding(p)
                .consumeWindowInsets(p),
        )
    }
}

@Composable
fun CrearCuentaContenido(onCrear: () -> Unit, modifier: Modifier = Modifier) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var documento by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    var acepto by rememberSaveable { mutableStateOf(false) }

    val errores = listOf(
        if (nombre.isBlank()) stringResource(R.string.error_obligatorio) else null,
        if (documento.count(Char::isDigit) < 5) stringResource(R.string.error_documento) else null,
        if (Validaciones.esCorreo(correo)) null else stringResource(R.string.error_correo),
        if (Validaciones.esTelefono(telefono)) null else stringResource(R.string.error_telefono),
        if (contrasena.length < MIN_CONTRASENA) stringResource(R.string.error_contrasena, MIN_CONTRASENA) else null,
        if (confirmar != contrasena || confirmar.isEmpty()) stringResource(R.string.error_no_coinciden) else null,
    )
    val valido = errores.all { it == null } && acepto

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        CampoTextoConError(nombre, { nombre = it }, stringResource(R.string.campo_nombre_completo), errores[0], obligatorio = true)
        CampoTextoConError(documento, { documento = it }, stringResource(R.string.campo_documento), errores[1], obligatorio = true, teclado = KeyboardType.Number)
        CampoTextoConError(correo, { correo = it.trim() }, stringResource(R.string.campo_correo), errores[2], obligatorio = true, teclado = KeyboardType.Email)
        CampoTextoConError(telefono, { telefono = it }, stringResource(R.string.campo_telefono), errores[3], obligatorio = true, teclado = KeyboardType.Phone)
        CampoTextoConError(contrasena, { contrasena = it }, stringResource(R.string.campo_contrasena), errores[4], obligatorio = true, teclado = KeyboardType.Password, esContrasena = true)
        CampoTextoConError(confirmar, { confirmar = it }, stringResource(R.string.campo_confirmar_contrasena), errores[5], obligatorio = true, teclado = KeyboardType.Password, esContrasena = true)
        CasillaVerificacion(stringResource(R.string.acepto_terminos), acepto, { acepto = it })
        BotonPrincipal(stringResource(R.string.crear_cuenta), onCrear, habilitado = valido)
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun CrearCuentaPreview() {
    PetsafeTheme { CrearCuentaContenido({}) }
}
