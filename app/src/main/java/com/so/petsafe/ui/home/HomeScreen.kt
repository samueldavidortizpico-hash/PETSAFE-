package com.so.petsafe.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.*
import com.so.petsafe.domain.modelo.*
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun HomeScreen(onNavegar: (String) -> Unit) {
    HomeContenido(haySesion = SesionFalsa.haySesion, mascotas = DatosDemo.misAnimales, onNavegar = onNavegar)
}

@Composable
fun HomeContenido(haySesion: Boolean, mascotas: List<Animal>, onNavegar: (String) -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(GrisSuave),
        contentPadding = PaddingValues(bottom = Dimens.EspacioXxl),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        item(key = "encabezado") { EncabezadoInicio(onBuscar = { onNavegar(Rutas.MIS_ANIMALES) }) }
        item(key = "emergencia") {
            TarjetaEmergencia(onClick = { onNavegar(Rutas.URGENCIAS) }, Modifier.padding(horizontal = Dimens.EspacioL))
        }
        item(key = "acciones") { AccionesRapidas(onNavegar) }
        item(key = "comunidad") { ResumenComunidad(onNavegar) }
        if (haySesion) item(key = "mascotas") { MisMascotas(mascotas, onNavegar) }
        item(key = "ayuda") {
            FilaNavegable(
                emoji = stringResource(R.string.emoji_ayuda),
                titulo = stringResource(R.string.home_centro_ayuda),
                descripcion = stringResource(R.string.home_centro_ayuda_desc),
                onClick = { onNavegar(Rutas.CENTRO_AYUDA) },
                modifier = Modifier.padding(horizontal = Dimens.EspacioL),
            )
        }
    }
}

@Composable
private fun EncabezadoInicio(onBuscar: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .padding(bottom = Dimens.TraslapeBuscador)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = Dimens.RadioEncabezado, bottomEnd = Dimens.RadioEncabezado))
                .background(Brush.linearGradient(listOf(Turquesa, Verde)))
                .statusBarsPadding()
                .padding(
                    start = Dimens.EspacioL,
                    end = Dimens.EspacioL,
                    top = Dimens.EspacioXl,
                    bottom = Dimens.TraslapeBuscador + Dimens.EspacioXxl,
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CajaEmoji(stringResource(R.string.emoji_huella), BlancoTranslucido, tamano = Dimens.CajaIconoGrande)
                Spacer(Modifier.width(Dimens.EspacioM))
                Column {
                    Text(
                        stringResource(R.string.app_nombre),
                        color = Blanco,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(stringResource(R.string.app_lema), color = BlancoSuave, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Surface(
            onClick = onBuscar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = Dimens.EspacioL)
                .fillMaxWidth()
                .height(Dimens.AlturaBuscador),
            shape = RoundedCornerShape(Dimens.RadioTarjeta),
            color = Blanco,
            shadowElevation = Dimens.ElevacionBarra,
        ) {
            Row(Modifier.padding(horizontal = Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Search, stringResource(R.string.cd_buscar), tint = TextoSecundario)
                Spacer(Modifier.width(Dimens.EspacioM))
                Text(stringResource(R.string.home_buscar), color = TextoSecundario, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun TarjetaEmergencia(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TarjetaBase(modifier.fillMaxWidth(), color = RojoEmergencia, radio = Dimens.RadioGrande) {
        Row(Modifier.padding(Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
            CajaEmoji(stringResource(R.string.emoji_sirena), BlancoTranslucido)
            Spacer(Modifier.width(Dimens.EspacioM))
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.home_emergencia_titulo),
                    color = Blanco,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(stringResource(R.string.home_emergencia_sub), color = BlancoSuave, style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(Dimens.RadioS),
                colors = ButtonDefaults.buttonColors(containerColor = Blanco, contentColor = RojoEmergencia),
            ) {
                Text(stringResource(R.string.home_solicitar_ayuda), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AccionesRapidas(onNavegar: (String) -> Unit) {
    Column(Modifier.padding(horizontal = Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
        TituloSeccion(stringResource(R.string.home_acciones))
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
            TarjetaAccion(stringResource(R.string.accion_registrar), Icons.Rounded.Pets, Turquesa, { onNavegar(Rutas.REGISTRAR) }, Modifier.weight(1f))
            TarjetaAccion(stringResource(R.string.accion_reportar_perdido), Icons.Rounded.Search, Naranja, { onNavegar(Rutas.REPORTE_PERDIDO) }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
            TarjetaAccion(stringResource(R.string.accion_encontrado), Icons.Rounded.Place, Ambar, { onNavegar(Rutas.REPORTE_ENCONTRADO) }, Modifier.weight(1f))
            TarjetaAccion(stringResource(R.string.accion_denunciar), Icons.Rounded.Report, Rojo, { onNavegar(Rutas.DENUNCIA) }, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ResumenComunidad(onNavegar: (String) -> Unit) {
    Column(Modifier.padding(horizontal = Dimens.EspacioL)) {
        TituloSeccion(
            stringResource(R.string.home_comunidad),
            accion = stringResource(R.string.ver_todo),
            onAccion = { onNavegar(Rutas.COMUNIDAD) },
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
            TarjetaNumero(
                stringResource(R.string.emoji_lupa), stringResource(R.string.home_num_perdidos), stringResource(R.string.home_perdidos_activos),
                RojoSuave, RojoOscuro, Modifier.weight(1f),
            ) { onNavegar(Rutas.PERDIDOS) }
            TarjetaNumero(
                stringResource(R.string.emoji_corazon), stringResource(R.string.home_num_adopcion), stringResource(R.string.home_en_adopcion),
                MoradoFondo, Morado, Modifier.weight(1f),
            ) { onNavegar(Rutas.ADOPCIONES) }
            TarjetaNumero(
                stringResource(R.string.emoji_huella), stringResource(R.string.home_num_ayuda), stringResource(R.string.home_necesitan_ayuda),
                AmbarSuave, AmbarOscuro, Modifier.weight(1f),
            ) { onNavegar(Rutas.COMUNIDAD) }
        }
    }
}

@Composable
private fun TarjetaNumero(emoji: String, numero: String, etiqueta: String, fondo: Color, texto: Color, modifier: Modifier, onClick: () -> Unit) {
    TarjetaBase(modifier, color = fondo, onClick = onClick) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.EspacioM),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(emoji, fontSize = Dimens.EmojiMediano)
            Text(numero, color = texto, fontWeight = FontWeight.Bold, fontSize = Dimens.NumeroGrande)
            Text(etiqueta, color = texto, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun MisMascotas(mascotas: List<Animal>, onNavegar: (String) -> Unit) {
    Column {
        TituloSeccion(
            stringResource(R.string.home_mis_mascotas),
            modifier = Modifier.padding(horizontal = Dimens.EspacioL),
            accion = stringResource(R.string.ver_todas),
            onAccion = { onNavegar(Rutas.MIS_ANIMALES) },
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.EspacioL),
            horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            items(mascotas, key = { it.id }) { animal ->
                TarjetaBase(
                    Modifier.width(Dimens.AnchoTarjetaMascota),
                    onClick = { onNavegar(Rutas.conId(Rutas.PERFIL_ANIMAL, animal.id)) },
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(Dimens.EspacioM),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioXs),
                    ) {
                        AvatarAnimal(animal.especie.emoji, animal.nombre, fondo = fondoTono(animal.estado.tono))
                        Text(animal.nombre, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                        EstadoBadge(etiquetaEstado(animal.estado), animal.estado.tono)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1300)
@Composable
private fun HomePreview() {
    PetsafeTheme { HomeContenido(haySesion = true, mascotas = DatosDemo.misAnimales.toList(), onNavegar = {}) }
}
