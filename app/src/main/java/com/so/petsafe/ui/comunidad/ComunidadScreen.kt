package com.so.petsafe.ui.comunidad

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.*
import com.so.petsafe.domain.modelo.*
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

private data class OpcionComunidad(
    @StringRes val emoji: Int,
    @StringRes val titulo: Int,
    @StringRes val descripcion: Int,
    val ruta: String?,
    val fondo: Color,
)

private val OPCIONES = listOf(
    OpcionComunidad(R.string.emoji_lupa, R.string.com_perdidos, R.string.com_perdidos_desc, Rutas.PERDIDOS, RojoSuave),
    OpcionComunidad(R.string.emoji_corazon, R.string.com_adopciones, R.string.com_adopciones_desc, Rutas.ADOPCIONES, MoradoFondo),
    OpcionComunidad(R.string.emoji_casa, R.string.com_sin_hogar, R.string.com_sin_hogar_desc, null, AmbarSuave),
    OpcionComunidad(R.string.emoji_huella, R.string.com_rescates, R.string.com_rescates_desc, null, TurquesaSuave),
    OpcionComunidad(R.string.emoji_manos, R.string.com_casos_ayuda, R.string.com_casos_ayuda_desc, null, NaranjaSuave),
    OpcionComunidad(R.string.emoji_alerta, R.string.com_denunciar, R.string.com_denunciar_desc, Rutas.DENUNCIA, RojoSuave),
)

@Composable
fun ComunidadScreen(onNavegar: (String) -> Unit) {
    ComunidadContenido(DatosDemo.ayudantes, onNavegar)
}

@Composable
fun ComunidadContenido(ayudantes: List<Ayudante>, onNavegar: (String) -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(GrisSuave),
        contentPadding = PaddingValues(bottom = Dimens.EspacioXxl),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        item(key = "encabezado") {
            EncabezadoSeccion(stringResource(R.string.com_titulo), stringResource(R.string.com_subtitulo))
        }
        items(OPCIONES, key = { it.titulo }) { opcion ->
            val titulo = stringResource(opcion.titulo)
            FilaNavegable(
                emoji = stringResource(opcion.emoji),
                titulo = titulo,
                descripcion = stringResource(opcion.descripcion),
                onClick = { onNavegar(opcion.ruta ?: Rutas.proximamente(titulo)) },
                modifier = Modifier.padding(horizontal = Dimens.EspacioL),
                fondoEmoji = opcion.fondo,
            )
        }
        item(key = "ayudantes-titulo") {
            Column(Modifier.padding(horizontal = Dimens.EspacioL, vertical = Dimens.EspacioS)) {
                Text(stringResource(R.string.com_ayudando), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(stringResource(R.string.com_reconocimiento), style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
            }
        }
        items(ayudantes, key = { it.nombre }) { FilaAyudante(it, Modifier.padding(horizontal = Dimens.EspacioL)) }
    }
}

@Composable
private fun FilaAyudante(ayudante: Ayudante, modifier: Modifier = Modifier) {
    TarjetaBase(modifier.fillMaxWidth()) {
        Row(Modifier.padding(Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
            val iniciales = ayudante.nombre.split(" ").mapNotNull { it.firstOrNull() }.joinToString("")
            AvatarAnimal(iniciales, ayudante.nombre)
            Spacer(Modifier.width(Dimens.EspacioM))
            Column(Modifier.weight(1f)) {
                Text(ayudante.nombre, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(ayudante.logro, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(stringResource(R.string.puntos, ayudante.puntos), fontWeight = FontWeight.Bold, color = Turquesa)
                Text(ayudante.insignia, style = MaterialTheme.typography.labelSmall, color = TextoSecundario)
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun ComunidadPreview() {
    PetsafeTheme { ComunidadContenido(DatosDemo.ayudantes, {}) }
}
