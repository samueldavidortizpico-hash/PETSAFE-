package com.so.petsafe.ui.veterinario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.Validaciones
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

/** Formulario clínico. Desde el modo veterinario queda "Verificado por veterinario"; desde el responsable, pendiente de verificación. */
@Composable
fun NuevoRegistroClinicoScreen(esVeterinario: Boolean, onVolver: () -> Unit) {
    var guardado by rememberSaveable { mutableStateOf(false) }
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.nuevo_registro_clinico), onVolver) }, containerColor = GrisSuave) { p ->
        if (guardado) {
            PantallaExito(
                titulo = stringResource(R.string.registro_guardado),
                mensaje = stringResource(if (esVeterinario) R.string.aviso_verificado else R.string.aviso_pendiente_verificacion),
                modifier = Modifier.padding(p),
            ) { BotonPrincipal(stringResource(R.string.volver), onVolver) }
        } else {
            NuevoRegistroContenido(
                esVeterinario,
                onGuardar = { guardado = true },
                modifier = Modifier
                    .padding(p)
                    .consumeWindowInsets(p),
            )
        }
    }
}

@Composable
fun NuevoRegistroContenido(esVeterinario: Boolean, onGuardar: () -> Unit, modifier: Modifier = Modifier) {
    var fecha by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var condicion by rememberSaveable { mutableStateOf("") }
    var esterilizacion by rememberSaveable { mutableStateOf<String?>(null) }
    // Campos libres opcionales, en el orden del formulario.
    val etiquetas = listOf(
        R.string.vet_vacunas, R.string.vet_diagnosticos, R.string.hist_enfermedades, R.string.hist_tratamientos,
        R.string.vet_medicamentos, R.string.vet_alergias, R.string.vet_procedimientos, R.string.campo_observaciones,
    )
    var libres by rememberSaveable { mutableStateOf(List(etiquetas.size) { "" }) }

    val req = stringResource(R.string.error_obligatorio)
    val errorPeso = when {
        peso.isBlank() -> req
        !Validaciones.esNumero(peso) -> stringResource(R.string.error_numero)
        else -> null
    }
    val valido = fecha.isNotBlank() && errorPeso == null && condicion.isNotBlank() && esterilizacion != null

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        Aviso(
            stringResource(if (esVeterinario) R.string.aviso_verificado else R.string.aviso_pendiente_verificacion),
            if (esVeterinario) Tono.VERDE else Tono.AMBAR,
            icono = Icons.Rounded.Verified,
        )
        CampoFecha(fecha, { fecha = it }, stringResource(R.string.campo_fecha))
        CampoTextoConError(peso, { peso = it }, stringResource(R.string.campo_peso), errorPeso, obligatorio = true, teclado = KeyboardType.Decimal)
        CampoTextoConError(edad, { edad = it }, stringResource(R.string.vet_edad_estimada), null)
        CampoTextoConError(condicion, { condicion = it }, stringResource(R.string.vet_condicion), if (condicion.isBlank()) req else null, obligatorio = true)
        etiquetas.forEachIndexed { i, etiqueta ->
            CampoTextoConError(libres[i], { v -> libres = libres.toMutableList().also { it[i] = v } }, stringResource(etiqueta), null, lineasMin = 2)
        }
        SelectorOpciones(
            stringResource(R.string.vet_esterilizacion),
            listOf(stringResource(R.string.si), stringResource(R.string.no), stringResource(R.string.no_se_sabe)),
            esterilizacion, { esterilizacion = it },
        )
        BotonPrincipal(stringResource(R.string.guardar_registro), onGuardar, habilitado = valido)
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun NuevoRegistroPreview() {
    PetsafeTheme { NuevoRegistroContenido(true, {}) }
}
