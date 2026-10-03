package com.so.petsafe.ui.urgencias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

private val OPCIONES_URGENCIA = listOf(
    R.string.emoji_perro to R.string.urg_herido,
    R.string.emoji_carro to R.string.urg_accidente,
    R.string.emoji_alerta to R.string.urg_maltrato,
    R.string.emoji_perro to R.string.urg_agresivo,
    R.string.emoji_casa to R.string.urg_abandonado,
    R.string.emoji_pregunta to R.string.urg_otra,
)

/** 3 estados en un composable: elegir situación → formulario → ayuda solicitada. */
@Composable
fun UrgenciasScreen(onVolver: () -> Unit) {
    var situacion by rememberSaveable { mutableStateOf<Int?>(null) }
    var solicitada by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val volver = {
        when {
            solicitada -> onVolver()
            situacion != null -> situacion = null
            else -> onVolver()
        }
    }
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.urgencias_titulo), volver) }, containerColor = GrisSuave) { p ->
        val mod = Modifier
            .padding(p)
            .consumeWindowInsets(p)
        when {
            solicitada -> AyudaSolicitada({ abrirWhatsApp(context) }, { marcarEmergencias(context) }, mod)
            situacion != null -> FormularioUrgencia(stringResource(situacion!!), { solicitada = true }, mod)
            else -> ElegirSituacion({ situacion = it }, mod)
        }
    }
}

@Composable
fun ElegirSituacion(onElegir: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        Text(stringResource(R.string.urg_pregunta), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        OPCIONES_URGENCIA.chunked(2).forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
                fila.forEach { (emoji, titulo) ->
                    TarjetaBase(Modifier.weight(1f), color = RojoSuave, onClick = { onElegir(titulo) }) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(Dimens.AlturaAccion)
                                .padding(Dimens.EspacioM),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(stringResource(emoji), fontSize = Dimens.EmojiMediano)
                            Text(stringResource(titulo), fontWeight = FontWeight.Bold, color = RojoOscuro, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
        Aviso(stringResource(R.string.urg_seguridad), Tono.AMBAR)
    }
}

@Composable
fun FormularioUrgencia(situacion: String, onSolicitar: () -> Unit, modifier: Modifier = Modifier) {
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var foto by rememberSaveable { mutableStateOf(false) }
    var video by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        EstadoBadge(situacion, Tono.ROJO)
        CampoTextoConError(
            ubicacion, { ubicacion = it }, stringResource(R.string.campo_ubicacion),
            if (ubicacion.isBlank()) stringResource(R.string.error_obligatorio) else null, obligatorio = true,
        )
        CampoTextoConError(descripcion, { descripcion = it }, stringResource(R.string.campo_descripcion), null, lineasMin = 3)
        BotonAdjunto(stringResource(R.string.agregar_foto), foto, { foto = true })
        BotonAdjunto(stringResource(R.string.agregar_video), video, { video = true }, esVideo = true)
        BotonPrincipal(stringResource(R.string.solicitar_ayuda), onSolicitar, habilitado = ubicacion.isNotBlank(), color = RojoEmergencia)
    }
}

@Composable
fun AyudaSolicitada(onWhatsApp: () -> Unit, onLlamar: () -> Unit, modifier: Modifier = Modifier) {
    PantallaExito(
        titulo = stringResource(R.string.ayuda_solicitada),
        mensaje = stringResource(R.string.ayuda_canales),
        modifier = modifier,
        icono = Icons.Rounded.SupportAgent,
        tono = Tono.ROJO,
    ) {
        BotonPrincipal(stringResource(R.string.contactar_whatsapp), onWhatsApp, color = Verde)
        BotonSecundario(stringResource(R.string.linea_123), onLlamar, color = RojoEmergencia, icono = Icons.Rounded.Call)
        Text(stringResource(R.string.ayuda_disclaimer), style = MaterialTheme.typography.bodySmall, color = TextoSecundario, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun UrgenciasPreview() {
    PetsafeTheme { ElegirSituacion({}) }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun AyudaSolicitadaPreview() {
    PetsafeTheme { AyudaSolicitada({}, {}) }
}
