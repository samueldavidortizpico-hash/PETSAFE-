package com.so.petsafe.ui.ayuda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Articulo
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun CentroAyudaScreen(onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    CentroAyudaContenido(DatosDemo.articulos, onNavegar, onVolver)
}

@Composable
fun CentroAyudaContenido(articulos: List<Articulo>, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.home_centro_ayuda), onVolver) }, containerColor = GrisSuave) { p ->
        LazyColumn(
            Modifier.padding(p),
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            item(key = "aviso") { Aviso(stringResource(R.string.ayuda_no_reemplaza), Tono.TURQUESA, icono = Icons.Rounded.HealthAndSafety) }
            item(key = "chatbot") {
                FilaNavegable(
                    emoji = stringResource(R.string.emoji_chat),
                    titulo = stringResource(R.string.chatbot_titulo),
                    descripcion = stringResource(R.string.chatbot_desc),
                    onClick = { onNavegar(Rutas.CHATBOT) },
                    fondoEmoji = VerdeSuave,
                )
            }
            item(key = "titulo") { TituloSeccion(stringResource(R.string.guias)) }
            items(articulos, key = { it.id }) { a ->
                FilaNavegable(a.emoji, a.titulo, null, { onNavegar(Rutas.conId(Rutas.ARTICULO, a.id)) })
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun CentroAyudaPreview() {
    PetsafeTheme { CentroAyudaContenido(DatosDemo.articulos, {}, {}) }
}
