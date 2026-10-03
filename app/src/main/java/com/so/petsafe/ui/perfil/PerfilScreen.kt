package com.so.petsafe.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.SesionFalsa
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun PerfilScreen(onNavegar: (String) -> Unit) {
    PerfilContenido(
        haySesion = SesionFalsa.haySesion,
        onNavegar = onNavegar,
        onCerrarSesion = { SesionFalsa.haySesion = false },
    )
}

@Composable
fun PerfilContenido(haySesion: Boolean, onNavegar: (String) -> Unit, onCerrarSesion: () -> Unit) {
    val pronto = { titulo: String -> onNavegar(Rutas.proximamente(titulo)) }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(GrisSuave),
        contentPadding = PaddingValues(bottom = Dimens.EspacioXxl),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        if (!haySesion) {
            item(key = "unete") { Unete(onNavegar) }
        } else {
            item(key = "encabezado") { EncabezadoSeccion(stringResource(R.string.mi_perfil), null) }
            item(key = "datos") { DatosUsuario({ pronto(it) }) }
            item(key = "actividad-t") { TituloSeccion(stringResource(R.string.mi_actividad), Modifier.padding(horizontal = Dimens.EspacioL)) }
            val actividad = listOf(
                Triple(R.string.emoji_huella, R.string.act_animales to R.string.act_animales_desc, Rutas.MIS_ANIMALES),
                Triple(R.string.emoji_corazon, R.string.act_solicitudes to R.string.act_solicitudes_desc, null),
                Triple(R.string.emoji_lupa, R.string.act_reportes to R.string.act_reportes_desc, Rutas.PERDIDOS),
                Triple(R.string.emoji_portapapeles, R.string.act_casos to R.string.act_casos_desc, Rutas.SEGUIMIENTO),
                Triple(R.string.emoji_medalla, R.string.act_comunidad to R.string.act_comunidad_desc, Rutas.COMUNIDAD),
            )
            items(actividad, key = { it.second.first }) { (emoji, textos, ruta) ->
                val titulo = stringResource(textos.first)
                FilaNavegable(
                    stringResource(emoji), titulo, stringResource(textos.second),
                    { onNavegar(ruta ?: Rutas.proximamente(titulo)) },
                    Modifier.padding(horizontal = Dimens.EspacioL),
                )
            }
            item(key = "config-t") { TituloSeccion(stringResource(R.string.configuracion), Modifier.padding(horizontal = Dimens.EspacioL)) }
            val config = listOf(
                R.string.emoji_campana to R.string.cfg_notificaciones,
                R.string.emoji_candado to R.string.cfg_privacidad,
                R.string.emoji_escudo to R.string.cfg_seguridad,
            )
            items(config, key = { it.second }) { (emoji, tituloRes) ->
                val titulo = stringResource(tituloRes)
                FilaNavegable(stringResource(emoji), titulo, null, { pronto(titulo) }, Modifier.padding(horizontal = Dimens.EspacioL))
            }
            item(key = "ayuda") {
                FilaNavegable(
                    stringResource(R.string.emoji_ayuda), stringResource(R.string.cfg_ayuda), null,
                    { onNavegar(Rutas.CENTRO_AYUDA) }, Modifier.padding(horizontal = Dimens.EspacioL),
                )
            }
        }
        item(key = "roles-t") { TituloSeccion(stringResource(R.string.otros_accesos), Modifier.padding(horizontal = Dimens.EspacioL)) }
        item(key = "vet") {
            FilaNavegable(
                stringResource(R.string.emoji_hospital), stringResource(R.string.modo_veterinario), stringResource(R.string.modo_veterinario_desc),
                { onNavegar(Rutas.VET_PANEL) }, Modifier.padding(horizontal = Dimens.EspacioL), fondoEmoji = VerdeSuave,
            )
        }
        item(key = "municipal") {
            FilaNavegable(
                stringResource(R.string.emoji_grafica), stringResource(R.string.indicadores_municipales), stringResource(R.string.indicadores_desc),
                { onNavegar(Rutas.MUNICIPAL) }, Modifier.padding(horizontal = Dimens.EspacioL), fondoEmoji = MoradoFondo,
            )
        }
        if (haySesion) item(key = "salir") {
            BotonSecundario(
                stringResource(R.string.cerrar_sesion), onCerrarSesion,
                Modifier.padding(horizontal = Dimens.EspacioL), color = Rojo, icono = Icons.AutoMirrored.Rounded.Logout,
            )
        }
    }
}

@Composable
private fun Unete(onNavegar: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(Dimens.EspacioXxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioL),
    ) {
        Spacer(Modifier.height(Dimens.EspacioXxl))
        AvatarAnimal(stringResource(R.string.emoji_huella), stringResource(R.string.app_nombre), tamano = Dimens.AvatarGrande)
        Text(stringResource(R.string.unete_titulo), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        Text(stringResource(R.string.unete_texto), textAlign = TextAlign.Center, color = TextoSecundario)
        BotonPrincipal(stringResource(R.string.iniciar_sesion), { onNavegar(Rutas.LOGIN) })
        BotonSecundario(stringResource(R.string.crear_cuenta), { onNavegar(Rutas.CREAR_CUENTA) })
    }
}

@Composable
private fun DatosUsuario(onEditar: (String) -> Unit) {
    val editar = stringResource(R.string.editar_perfil)
    TarjetaBase(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.EspacioL)
    ) {
        Row(Modifier.padding(Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
            AvatarAnimal(stringResource(R.string.iniciales_usuario), SesionFalsa.NOMBRE, tamano = Dimens.CajaIconoGrande)
            Spacer(Modifier.width(Dimens.EspacioM))
            Column(Modifier.weight(1f)) {
                Text(SesionFalsa.NOMBRE, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(SesionFalsa.CORREO, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                Text(SesionFalsa.TELEFONO, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
            }
            TextButton(onClick = { onEditar(editar) }) { Text(editar, color = Turquesa) }
        }
    }
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun PerfilConSesionPreview() {
    PetsafeTheme { PerfilContenido(true, {}, {}) }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun PerfilSinSesionPreview() {
    PetsafeTheme { PerfilContenido(false, {}, {}) }
}
