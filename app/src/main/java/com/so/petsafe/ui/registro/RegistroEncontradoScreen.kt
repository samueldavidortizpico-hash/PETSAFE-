package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

// En la fase del taller esta pantalla se reemplaza por el módulo offline.
@Composable
fun RegistroEncontradoScreen(onReemplazar: (String) -> Unit, onVolver: () -> Unit) {
    var registrado by rememberSaveable { mutableStateOf(false) }
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.registro_encontrado_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        if (registrado) {
            PantallaExito(
                titulo = stringResource(R.string.animal_registrado),
                mensaje = stringResource(R.string.animal_registrado_msg),
                modifier = Modifier.padding(p),
            ) {
                BotonPrincipal(stringResource(R.string.volver_inicio), { onReemplazar(Rutas.HOME) })
            }
        } else {
            RegistroEncontradoContenido(
                onRegistrar = { registrado = true },
                modifier = Modifier
                    .padding(p)
                    .consumeWindowInsets(p),
            )
        }
    }
}

@Composable
fun RegistroEncontradoContenido(onRegistrar: () -> Unit, modifier: Modifier = Modifier) {
    var foto by rememberSaveable { mutableStateOf(false) }
    var especie by rememberSaveable { mutableStateOf<String?>(null) }
    var sexo by rememberSaveable { mutableStateOf<String?>(null) }
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var raza by rememberSaveable { mutableStateOf("") }
    var observaciones by rememberSaveable { mutableStateOf("") }
    var intento by rememberSaveable { mutableStateOf(false) }

    val valido = foto && especie != null && sexo != null && ubicacion.isNotBlank() && fecha.isNotBlank()

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        Aviso(stringResource(R.string.registro_simplificado), Tono.TURQUESA)
        BotonAdjunto(
            stringResource(R.string.foto_obligatoria), foto, { foto = true },
            error = if (intento && !foto) stringResource(R.string.error_foto) else null,
        )
        SelectorOpciones(stringResource(R.string.campo_especie), stringArrayResource(R.array.especies_registro).toList(), especie, { especie = it })
        SelectorOpciones(
            stringResource(R.string.campo_sexo),
            stringArrayResource(R.array.sexos).toList() + stringResource(R.string.no_identificado),
            sexo, { sexo = it },
        )
        CampoTextoConError(
            ubicacion, { ubicacion = it }, stringResource(R.string.campo_ubicacion_encontrado),
            if (ubicacion.isBlank()) stringResource(R.string.error_obligatorio) else null, obligatorio = true,
        )
        CampoFecha(fecha, { fecha = it }, stringResource(R.string.campo_fecha_hallazgo))
        CampoTextoConError(raza, { raza = it }, stringResource(R.string.campo_raza_descripcion), null)
        CampoTextoConError(observaciones, { observaciones = it }, stringResource(R.string.campo_observaciones), null, lineasMin = 3)
        BotonPrincipal(
            stringResource(R.string.registrar_animal),
            onClick = { intento = true; if (valido) onRegistrar() },
            color = if (valido) Turquesa else TextoSecundario,
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun RegistroEncontradoPreview() {
    PetsafeTheme { RegistroEncontradoContenido({}) }
}
