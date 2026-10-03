package com.so.petsafe.ui.ayuda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Articulo
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun ArticuloScreen(id: String, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    val articulo = DatosDemo.articulo(id)
    if (articulo == null) PantallaProximamente(stringResource(R.string.home_centro_ayuda), onVolver)
    else ArticuloContenido(articulo, onNavegar, onVolver)
}

@Composable
fun ArticuloContenido(articulo: Articulo, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.guia), onVolver) }, containerColor = GrisSuave) { p ->
        Column(
            Modifier
                .padding(p)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            CajaEmoji(articulo.emoji, TurquesaSuave, tamano = Dimens.CajaIconoGrande)
            Text(articulo.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextoPrincipal)
            TarjetaBase(Modifier.fillMaxWidth()) {
                Text(articulo.cuerpo, style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal, modifier = Modifier.padding(Dimens.EspacioL))
            }
            Aviso(stringResource(R.string.ayuda_no_reemplaza), Tono.AMBAR)
            BotonPrincipal(stringResource(R.string.hablar_asistente), { onNavegar(Rutas.CHATBOT) })
            BotonSecundario(stringResource(R.string.home_solicitar_ayuda), { onNavegar(Rutas.URGENCIAS) }, color = RojoEmergencia)
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun ArticuloPreview() {
    PetsafeTheme { ArticuloContenido(DatosDemo.articulos.first(), {}, {}) }
}
