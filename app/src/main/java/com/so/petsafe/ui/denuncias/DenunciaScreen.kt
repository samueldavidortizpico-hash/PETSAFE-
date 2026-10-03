package com.so.petsafe.ui.denuncias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Gavel
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

// Sin selector de nivel (Leve/Medio/Grave): lo asigna un administrador.
@Composable
fun DenunciaScreen(onEnviada: () -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.denuncia_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        DenunciaContenido(
            onEnviar = onEnviada,
            modifier = Modifier
                .padding(p)
                .consumeWindowInsets(p),
        )
    }
}

@Composable
fun DenunciaContenido(onEnviar: () -> Unit, modifier: Modifier = Modifier) {
    var tipo by rememberSaveable { mutableStateOf<String?>(null) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var animal by rememberSaveable { mutableStateOf("") }
    var responsable by rememberSaveable { mutableStateOf("") }
    var fotos by rememberSaveable { mutableStateOf(false) }
    var video by rememberSaveable { mutableStateOf(false) }
    val req = stringResource(R.string.error_obligatorio)
    val valido = tipo != null && descripcion.isNotBlank() && ubicacion.isNotBlank()

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        Aviso(stringResource(R.string.denuncia_presuncion), Tono.AMBAR, icono = Icons.Rounded.Gavel)
        SelectorOpciones(stringResource(R.string.campo_tipo_situacion), stringArrayResource(R.array.tipos_situacion).toList(), tipo, { tipo = it })
        CampoTextoConError(descripcion, { descripcion = it }, stringResource(R.string.campo_descripcion), if (descripcion.isBlank()) req else null, obligatorio = true, lineasMin = 3)
        CampoTextoConError(ubicacion, { ubicacion = it }, stringResource(R.string.campo_ubicacion), if (ubicacion.isBlank()) req else null, obligatorio = true)
        CampoTextoConError(animal, { animal = it }, stringResource(R.string.campo_animal_involucrado), null)
        CampoTextoConError(responsable, { responsable = it }, stringResource(R.string.campo_datos_responsable), null)
        BotonAdjunto(stringResource(R.string.agregar_fotos), fotos, { fotos = true })
        BotonAdjunto(stringResource(R.string.agregar_video), video, { video = true }, esVideo = true)
        Aviso(stringResource(R.string.denuncia_nivel_admin), Tono.TURQUESA)
        BotonPrincipal(stringResource(R.string.enviar_reporte), onEnviar, habilitado = valido, color = Rojo)
    }
}

@Composable
fun DenunciaEnviadaScreen(onReemplazar: (String) -> Unit) {
    Scaffold(containerColor = GrisSuave) { p ->
        PantallaExito(
            titulo = stringResource(R.string.reporte_recibido),
            mensaje = stringResource(R.string.caso_sera_revisado),
            modifier = Modifier.padding(p),
        ) {
            BotonPrincipal(stringResource(R.string.ver_seguimiento), { onReemplazar(Rutas.SEGUIMIENTO) })
            BotonSecundario(stringResource(R.string.volver_inicio), { onReemplazar(Rutas.HOME) })
        }
    }
}

@Preview(showBackground = true, heightDp = 1100)
@Composable
private fun DenunciaPreview() {
    PetsafeTheme { DenunciaContenido({}) }
}

@Preview(showBackground = true)
@Composable
private fun DenunciaEnviadaPreview() {
    PetsafeTheme { DenunciaEnviadaScreen {} }
}
