package com.so.petsafe.ui.adopciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.*
import com.so.petsafe.domain.modelo.*
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun SolicitudAdopcionScreen(id: String, onEnviada: () -> Unit, onVolver: () -> Unit) {
    val nombre = DatosDemo.adopcion(id)?.nombre.orEmpty()
    Scaffold(
        topBar = { EncabezadoVolver(stringResource(R.string.solicitud_titulo), onVolver) },
        containerColor = GrisSuave,
    ) { p ->
        SolicitudContenido(
            nombreAnimal = nombre,
            onEnviar = onEnviada,
            modifier = Modifier
                .padding(p)
                .consumeWindowInsets(p),
        )
    }
}

@Composable
fun SolicitudContenido(nombreAnimal: String, onEnviar: () -> Unit, modifier: Modifier = Modifier) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var ocupacion by rememberSaveable { mutableStateOf("") }
    var economia by rememberSaveable { mutableStateOf<String?>(null) }
    var vivienda by rememberSaveable { mutableStateOf<String?>(null) }
    var tenencia by rememberSaveable { mutableStateOf<String?>(null) }
    var permiten by rememberSaveable { mutableStateOf<String?>(null) }
    var personas by rememberSaveable { mutableStateOf("") }
    var ninos by rememberSaveable { mutableStateOf<String?>(null) }
    var otros by rememberSaveable { mutableStateOf<String?>(null) }
    var experiencia by rememberSaveable { mutableStateOf("") }
    var tiempo by rememberSaveable { mutableStateOf("") }
    var motivo by rememberSaveable { mutableStateOf("") }
    var confirmo by rememberSaveable { mutableStateOf(false) }

    val req = stringResource(R.string.error_obligatorio)
    val edadNum = edad.toIntOrNull()
    val errorEdad = when {
        edad.isBlank() -> req
        edadNum == null -> stringResource(R.string.error_numero)
        edadNum < 18 -> stringResource(R.string.error_mayor_edad)
        else -> null
    }
    val errorPersonas = if ((personas.toIntOrNull() ?: 0) < 1) stringResource(R.string.error_numero) else null
    fun obligatorio(v: String) = if (v.isBlank()) req else null

    val valido = errorEdad == null && errorPersonas == null && confirmo &&
        listOf(nombre, ocupacion, experiencia, tiempo, motivo).all { it.isNotBlank() } &&
        listOf(economia, vivienda, tenencia, permiten, ninos, otros).all { it != null }

    val siNo = listOf(stringResource(R.string.si), stringResource(R.string.no))

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        if (nombreAnimal.isNotBlank()) Aviso(stringResource(R.string.solicitud_para, nombreAnimal), Tono.MORADO)
        CampoTextoConError(nombre, { nombre = it }, stringResource(R.string.campo_nombre), obligatorio(nombre), obligatorio = true)
        CampoTextoConError(edad, { edad = it.filter(Char::isDigit) }, stringResource(R.string.campo_edad), errorEdad, obligatorio = true, teclado = KeyboardType.Number)
        CampoTextoConError(ocupacion, { ocupacion = it }, stringResource(R.string.campo_ocupacion), obligatorio(ocupacion), obligatorio = true)
        SelectorOpciones(
            stringResource(R.string.campo_economia),
            listOf(stringResource(R.string.op_estable), stringResource(R.string.op_variable), stringResource(R.string.op_prefiero_no)),
            economia, { economia = it },
        )
        SelectorOpciones(
            stringResource(R.string.campo_vivienda),
            listOf(stringResource(R.string.op_casa), stringResource(R.string.op_apartamento), stringResource(R.string.op_finca)),
            vivienda, { vivienda = it },
        )
        SelectorOpciones(
            stringResource(R.string.campo_tenencia),
            listOf(stringResource(R.string.op_propia), stringResource(R.string.op_arrendada)),
            tenencia, { tenencia = it },
        )
        SelectorOpciones(
            stringResource(R.string.campo_permiten),
            siNo + stringResource(R.string.op_no_aplica),
            permiten, { permiten = it },
        )
        CampoTextoConError(personas, { personas = it.filter(Char::isDigit) }, stringResource(R.string.campo_personas), errorPersonas, obligatorio = true, teclado = KeyboardType.Number)
        SelectorOpciones(stringResource(R.string.campo_ninos), siNo, ninos, { ninos = it })
        SelectorOpciones(stringResource(R.string.campo_otros_animales), siNo, otros, { otros = it })
        CampoTextoConError(experiencia, { experiencia = it }, stringResource(R.string.campo_experiencia), obligatorio(experiencia), obligatorio = true, lineasMin = 2)
        CampoTextoConError(tiempo, { tiempo = it }, stringResource(R.string.campo_tiempo), obligatorio(tiempo), obligatorio = true)
        CampoTextoConError(motivo, { motivo = it }, stringResource(R.string.campo_motivo), obligatorio(motivo), obligatorio = true, lineasMin = 3)
        CasillaVerificacion(stringResource(R.string.confirmo_veraz), confirmo, { confirmo = it })
        BotonPrincipal(stringResource(R.string.enviar_solicitud), onEnviar, habilitado = valido, color = Morado)
    }
}

/** Éxito: la solicitud queda en evaluación. Nunca se muestra como aprobada. */
@Composable
fun AdopcionEnviadaScreen(onReemplazar: (String) -> Unit) {
    Scaffold(containerColor = GrisSuave) { p ->
        PantallaExito(
            titulo = stringResource(R.string.solicitud_enviada),
            mensaje = stringResource(R.string.solicitud_enviada_msg),
            modifier = Modifier.padding(p),
            icono = Icons.Rounded.HourglassTop,
            tono = Tono.MORADO,
        ) {
            BotonPrincipal(stringResource(R.string.ver_mas_animales), { onReemplazar(Rutas.ADOPCIONES) }, color = Morado)
            BotonSecundario(stringResource(R.string.volver_inicio), { onReemplazar(Rutas.HOME) })
        }
    }
}

@Preview(showBackground = true, heightDp = 1600)
@Composable
private fun SolicitudPreview() {
    PetsafeTheme { SolicitudContenido("Simba", {}) }
}

@Preview(showBackground = true)
@Composable
private fun AdopcionEnviadaPreview() {
    PetsafeTheme { AdopcionEnviadaScreen {} }
}
