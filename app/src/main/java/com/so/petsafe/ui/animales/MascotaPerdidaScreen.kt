package com.so.petsafe.ui.animales

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
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.EstadoAnimal
import com.so.petsafe.domain.modelo.Hito
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun MascotaPerdidaScreen(id: String, onVolver: () -> Unit) {
    val animal = DatosDemo.animal(id)
    MascotaPerdidaContenido(
        nombre = animal?.nombre.orEmpty(),
        perdida = animal?.estado == EstadoAnimal.PERDIDO,
        avistamientos = DatosDemo.avistamientos,
        onMarcarPerdida = { DatosDemo.actualizarEstado(id, EstadoAnimal.PERDIDO) },
        onMarcarEncontrada = { DatosDemo.actualizarEstado(id, EstadoAnimal.ACTIVO) },
        onVolver = onVolver,
    )
}

@Composable
fun MascotaPerdidaContenido(
    nombre: String,
    perdida: Boolean,
    avistamientos: List<Hito>,
    onMarcarPerdida: () -> Unit,
    onMarcarEncontrada: () -> Unit,
    onVolver: () -> Unit,
) {
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var foto by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.marcar_perdido), onVolver) }, containerColor = GrisSuave) { p ->
        Column(
            Modifier
                .padding(p)
                .consumeWindowInsets(p)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            if (perdida) {
                Aviso(stringResource(R.string.perdida_activa, nombre), Tono.ROJO)
                TituloSeccion(stringResource(R.string.avistamientos))
                TarjetaBase(Modifier.fillMaxWidth()) { LineaTiempo(avistamientos, Modifier.padding(Dimens.EspacioL)) }
                BotonPrincipal(stringResource(R.string.marcar_encontrada), onMarcarEncontrada, color = Verde)
            } else {
                Text(stringResource(R.string.perdida_instruccion, nombre), color = TextoSecundario)
                CampoTextoConError(
                    ubicacion, { ubicacion = it }, stringResource(R.string.campo_ultima_ubicacion),
                    if (ubicacion.isBlank()) stringResource(R.string.error_obligatorio) else null, obligatorio = true,
                )
                CampoFecha(fecha, { fecha = it }, stringResource(R.string.campo_fecha))
                BotonAdjunto(stringResource(R.string.agregar_foto), foto, { foto = true })
                BotonPrincipal(
                    stringResource(R.string.marcar_como_perdida),
                    onMarcarPerdida,
                    habilitado = ubicacion.isNotBlank() && fecha.isNotBlank(),
                    color = Rojo,
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun MascotaPerdidaPreview() {
    PetsafeTheme { MascotaPerdidaContenido("Max", true, DatosDemo.avistamientos, {}, {}, {}) }
}
