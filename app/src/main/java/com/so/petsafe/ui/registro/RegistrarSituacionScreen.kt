package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

private data class Situacion(val emoji: Int, val titulo: Int, val descripcion: Int, val ruta: String, val fondo: androidx.compose.ui.graphics.Color)

private val SITUACIONES = listOf(
    Situacion(R.string.emoji_casa, R.string.sit_propietario, R.string.sit_propietario_desc, Rutas.PASO1, TurquesaSuave),
    Situacion(R.string.emoji_pin, R.string.sit_encontrado, R.string.sit_encontrado_desc, Rutas.REGISTRO_ENCONTRADO, AmbarSuave),
    Situacion(R.string.emoji_lupa, R.string.sit_perdido, R.string.sit_perdido_desc, Rutas.REPORTE_PERDIDO, RojoSuave),
    Situacion(R.string.emoji_huella, R.string.sit_rescatado, R.string.sit_rescatado_desc, Rutas.REGISTRO_ENCONTRADO, VerdeSuave),
)

@Composable
fun RegistrarSituacionScreen(onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.accion_registrar), onVolver) }, containerColor = GrisSuave) { p ->
        LazyColumn(
            Modifier.padding(p),
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            item(key = "pregunta") {
                Text(
                    stringResource(R.string.sit_pregunta),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal,
                )
            }
            items(SITUACIONES, key = { it.titulo }) { s ->
                FilaNavegable(
                    emoji = stringResource(s.emoji),
                    titulo = stringResource(s.titulo),
                    descripcion = stringResource(s.descripcion),
                    onClick = { onNavegar(s.ruta) },
                    fondoEmoji = s.fondo,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrarSituacionPreview() {
    PetsafeTheme { RegistrarSituacionScreen({}, {}) }
}
