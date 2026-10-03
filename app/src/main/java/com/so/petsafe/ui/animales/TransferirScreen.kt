package com.so.petsafe.ui.animales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.SesionFalsa
import com.so.petsafe.domain.Validaciones
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun TransferirScreen(id: String, onVolver: () -> Unit) {
    val animal = DatosDemo.animal(id)
    var pendiente by rememberSaveable { mutableStateOf(false) }
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.transferir_responsable), onVolver) }, containerColor = GrisSuave) { p ->
        if (pendiente) {
            PantallaExito(
                titulo = stringResource(R.string.transferencia_pendiente),
                mensaje = stringResource(R.string.transferencia_pendiente_msg),
                modifier = Modifier.padding(p),
                icono = Icons.Rounded.HourglassTop,
                tono = Tono.AMBAR,
            ) { BotonSecundario(stringResource(R.string.volver), onVolver) }
        } else {
            TransferirContenido(
                nombreAnimal = animal?.nombre.orEmpty(),
                responsable = animal?.responsable ?: SesionFalsa.NOMBRE,
                onSolicitar = { pendiente = true },
                modifier = Modifier
                    .padding(p)
                    .consumeWindowInsets(p),
            )
        }
    }
}

@Composable
fun TransferirContenido(nombreAnimal: String, responsable: String, onSolicitar: () -> Unit, modifier: Modifier = Modifier) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var documento by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    val req = stringResource(R.string.error_obligatorio)
    val errorDoc = if (documento.count(Char::isDigit) < 5) stringResource(R.string.error_documento) else null
    val errorCorreo = if (Validaciones.esCorreo(correo)) null else stringResource(R.string.error_correo)
    val valido = nombre.isNotBlank() && errorDoc == null && errorCorreo == null

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        TarjetaBase(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(Dimens.EspacioL)) {
                if (nombreAnimal.isNotBlank()) FilaDato(stringResource(R.string.nfc_animal), nombreAnimal)
                FilaDato(stringResource(R.string.responsable_actual), responsable)
            }
        }
        TituloSeccion(stringResource(R.string.nuevo_responsable))
        CampoTextoConError(nombre, { nombre = it }, stringResource(R.string.campo_nombre_completo), if (nombre.isBlank()) req else null, obligatorio = true)
        CampoTextoConError(documento, { documento = it }, stringResource(R.string.campo_documento), errorDoc, obligatorio = true, teclado = KeyboardType.Number)
        CampoTextoConError(correo, { correo = it }, stringResource(R.string.campo_correo), errorCorreo, obligatorio = true, teclado = KeyboardType.Email)
        Aviso(stringResource(R.string.transferir_aviso), Tono.AMBAR, icono = Icons.Rounded.Warning)
        BotonPrincipal(stringResource(R.string.solicitar_transferencia), onSolicitar, habilitado = valido)
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun TransferirPreview() {
    PetsafeTheme { TransferirContenido("Toby", "Carlos Mendez", {}) }
}
