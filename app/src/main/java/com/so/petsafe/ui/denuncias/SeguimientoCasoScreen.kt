package com.so.petsafe.ui.denuncias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Hito
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

// No se muestra "Escalar caso" ni se afirma notificación a ninguna autoridad.
@Composable
fun SeguimientoCasoScreen(onVolver: () -> Unit) {
    var evidencias by rememberSaveable { mutableIntStateOf(0) }
    var observando by remember { mutableStateOf(false) }
    SeguimientoContenido(
        hitos = DatosDemo.seguimientoCaso,
        evidencias = evidencias,
        onEvidencia = { evidencias++ },
        onObservacion = { observando = true },
        onVolver = onVolver,
    )
    if (observando) DialogoObservacion(
        onGuardar = { evidencias++; observando = false },
        onCerrar = { observando = false },
    )
}

@Composable
fun SeguimientoContenido(
    hitos: List<Hito>,
    evidencias: Int,
    onEvidencia: () -> Unit,
    onObservacion: () -> Unit,
    onVolver: () -> Unit,
) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.seguimiento_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        Column(
            Modifier
                .padding(p)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            TarjetaBase(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioXs)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.caso_numero), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextoPrincipal, modifier = Modifier.weight(1f))
                        EstadoBadge(stringResource(R.string.en_verificacion), Tono.AMBAR)
                    }
                    FilaDato(stringResource(R.string.nfc_animal), stringResource(R.string.caso_animal))
                    FilaDato(stringResource(R.string.caso_tipo_etiqueta), stringResource(R.string.caso_tipo))
                    FilaDato(stringResource(R.string.caso_reportado_por_etiqueta), stringResource(R.string.caso_reportado_por))
                    FilaDato(stringResource(R.string.caso_nivel_etiqueta), stringResource(R.string.caso_nivel))
                }
            }
            TituloSeccion(stringResource(R.string.linea_tiempo))
            TarjetaBase(Modifier.fillMaxWidth()) { LineaTiempo(hitos, Modifier.padding(Dimens.EspacioL)) }
            TituloSeccion(stringResource(R.string.agregar_evidencia))
            BotonAdjunto(stringResource(R.string.agregar_fotos), false, onEvidencia)
            BotonAdjunto(stringResource(R.string.agregar_video), false, onEvidencia, esVideo = true)
            BotonSecundario(stringResource(R.string.agregar_observacion), onObservacion, icono = Icons.Rounded.EditNote)
            if (evidencias > 0) Aviso(stringResource(R.string.evidencias_agregadas, evidencias), Tono.VERDE)
        }
    }
}

@Composable
private fun DialogoObservacion(onGuardar: () -> Unit, onCerrar: () -> Unit) {
    var texto by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(stringResource(R.string.agregar_observacion)) },
        text = { CampoTextoConError(texto, { texto = it }, stringResource(R.string.campo_observaciones), null, lineasMin = 3) },
        confirmButton = { TextButton(onClick = onGuardar, enabled = texto.isNotBlank()) { Text(stringResource(R.string.guardar)) } },
        dismissButton = { TextButton(onClick = onCerrar) { Text(stringResource(R.string.cancelar)) } },
    )
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun SeguimientoPreview() {
    PetsafeTheme { SeguimientoContenido(DatosDemo.seguimientoCaso, 0, {}, {}, {}) }
}
