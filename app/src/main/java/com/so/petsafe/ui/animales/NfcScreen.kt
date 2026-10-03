package com.so.petsafe.ui.animales

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Nfc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*
import kotlinx.coroutines.delay

private const val DURACION_ESCANEO_MS = 2500L
private const val PULSO_MS = 900

// Solo UI en esta fase: la lectura NFC real llega en una fase posterior.
@Composable
fun NfcScreen(id: String, onVolver: () -> Unit) {
    val animal = DatosDemo.animal(id)
    var escaneando by rememberSaveable { mutableStateOf(false) }
    var leido by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(escaneando) {
        if (escaneando) {
            delay(DURACION_ESCANEO_MS)
            escaneando = false
            leido = true
        }
    }
    NfcContenido(
        chip = animal?.chip.orEmpty().ifBlank { stringResource(R.string.sin_dato) },
        nombre = animal?.nombre.orEmpty(),
        escaneando = escaneando,
        leido = leido,
        onEscanear = { leido = false; escaneando = true },
        onVolver = onVolver,
    )
}

@Composable
fun NfcContenido(chip: String, nombre: String, escaneando: Boolean, leido: Boolean, onEscanear: () -> Unit, onVolver: () -> Unit) {
    val pulso = rememberInfiniteTransition(label = "nfc")
    val escala by pulso.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(PULSO_MS), RepeatMode.Reverse),
        label = "escala",
    )
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.nfc_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        Column(
            Modifier
                .padding(p)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.EspacioL),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            TarjetaBase(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioXs)) {
                    FilaDato(stringResource(R.string.nfc_id), chip)
                    if (nombre.isNotBlank()) FilaDato(stringResource(R.string.nfc_animal), nombre)
                    EstadoBadge(stringResource(R.string.nfc_vinculado), Tono.VERDE)
                }
            }
            Spacer(Modifier.height(Dimens.EspacioL))
            Box(
                Modifier
                    .size(Dimens.CirculoNfc)
                    .graphicsLayer {
                        val s = if (escaneando) escala else 1f
                        scaleX = s
                        scaleY = s
                    }
                    .clip(CircleShape)
                    .background(if (leido) VerdeSuave else TurquesaSuave),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Nfc, contentDescription = null, tint = if (leido) Verde else Turquesa, modifier = Modifier.size(Dimens.IconoXl))
            }
            Text(
                stringResource(
                    when {
                        escaneando -> R.string.nfc_acerca
                        leido -> R.string.nfc_leido
                        else -> R.string.nfc_instruccion
                    }
                ),
                textAlign = TextAlign.Center,
                fontWeight = if (leido) FontWeight.Bold else FontWeight.Normal,
                color = if (leido) VerdeOscuro else TextoSecundario,
            )
            BotonPrincipal(stringResource(R.string.nfc_escanear), onEscanear, habilitado = !escaneando)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NfcPreview() {
    PetsafeTheme { NfcContenido("ARG-2023-00847", "Toby", false, false, {}, {}) }
}
