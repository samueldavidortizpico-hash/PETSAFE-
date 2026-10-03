package com.so.petsafe.ui.comunidad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.*
import com.so.petsafe.domain.modelo.*
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

/** Reporte de animal perdido o encontrado (mismo formulario, distinto modo). */
@Composable
fun ReporteScreen(esPerdido: Boolean, onReemplazar: (String) -> Unit, onVolver: () -> Unit) {
    var enviado by rememberSaveable { mutableStateOf(false) }
    val titulo = stringResource(if (esPerdido) R.string.reporte_perdido_titulo else R.string.reporte_encontrado_titulo)
    Scaffold(topBar = { EncabezadoVolver(titulo, onVolver) }, containerColor = GrisSuave) { p ->
        if (enviado) {
            PantallaExito(
                titulo = stringResource(R.string.reporte_exito),
                mensaje = stringResource(R.string.reporte_exito_msg),
                modifier = Modifier.padding(p),
            ) {
                BotonPrincipal(stringResource(R.string.ver_reportes), { onReemplazar(Rutas.PERDIDOS) })
                BotonSecundario(stringResource(R.string.volver_inicio), { onReemplazar(Rutas.HOME) })
            }
        } else {
            ReporteFormulario(
                esPerdido,
                Modifier
                    .padding(p)
                    .consumeWindowInsets(p),
            ) { reporte ->
                DatosDemo.reportes.add(0, reporte)
                enviado = true
            }
        }
    }
}

@Composable
fun ReporteFormulario(esPerdido: Boolean, modifier: Modifier = Modifier, onEnviar: (ReporteComunidad) -> Unit) {
    var animal by rememberSaveable { mutableStateOf("") }
    var foto by rememberSaveable { mutableStateOf(false) }
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var contacto by rememberSaveable { mutableStateOf("") }

    val req = stringResource(R.string.error_obligatorio)
    fun obligatorio(v: String) = if (v.isBlank()) req else null
    val valido = listOf(animal, ubicacion, fecha, descripcion, contacto).all { it.isNotBlank() }

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        CampoTextoConError(animal, { animal = it }, stringResource(R.string.campo_animal), obligatorio(animal), obligatorio = true)
        BotonAdjunto(stringResource(R.string.agregar_foto), foto, { foto = true })
        CampoTextoConError(
            ubicacion, { ubicacion = it },
            stringResource(if (esPerdido) R.string.campo_ultima_ubicacion else R.string.campo_ubicacion_encontrado),
            obligatorio(ubicacion), obligatorio = true,
        )
        CampoFecha(fecha, { fecha = it }, stringResource(R.string.campo_fecha))
        CampoTextoConError(descripcion, { descripcion = it }, stringResource(R.string.campo_descripcion), obligatorio(descripcion), obligatorio = true, lineasMin = 3)
        CampoTextoConError(contacto, { contacto = it }, stringResource(R.string.campo_contacto), obligatorio(contacto), obligatorio = true)
        Aviso(stringResource(R.string.reporte_privacidad), Tono.TURQUESA)
        BotonPrincipal(
            stringResource(R.string.publicar_reporte),
            onClick = {
                onEnviar(
                    ReporteComunidad(
                        id = "r${System.currentTimeMillis()}",
                        perdido = esPerdido,
                        nombre = if (esPerdido) animal.trim() else null,
                        especie = Especie.OTRO,
                        raza = if (esPerdido) "" else animal.trim(),
                        zona = ubicacion.trim(),
                        fecha = fecha,
                        descripcion = descripcion.trim(),
                    )
                )
            },
            habilitado = valido,
            color = if (esPerdido) Naranja else Turquesa,
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun ReportePreview() {
    PetsafeTheme { ReporteFormulario(esPerdido = true) {} }
}
