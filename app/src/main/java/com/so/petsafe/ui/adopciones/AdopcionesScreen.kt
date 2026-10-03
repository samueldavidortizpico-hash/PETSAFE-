package com.so.petsafe.ui.adopciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.*
import com.so.petsafe.domain.modelo.*
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun AdopcionesScreen(onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    AdopcionesContenido(DatosDemo.adopciones, onNavegar, onVolver)
}

@Composable
fun AdopcionesContenido(animales: List<AnimalAdopcion>, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.com_adopciones), onVolver) }, containerColor = GrisSuave) { p ->
        LazyColumn(
            Modifier.padding(p),
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            item(key = "aviso") { Aviso(stringResource(R.string.adopciones_aviso), Tono.MORADO, icono = Icons.Rounded.Favorite) }
            items(animales, key = { it.id }) { a ->
                TarjetaBase(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarAnimal(a.especie.emoji, a.tipo, tamano = Dimens.CajaIconoGrande, fondo = MoradoFondo)
                            Spacer(Modifier.width(Dimens.EspacioM))
                            Column(Modifier.weight(1f)) {
                                Text(a.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                                Text(stringResource(R.string.formato_tres, a.edad, a.raza, a.sexo), style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                            EstadoBadge(
                                stringResource(R.string.esterilizado_valor, stringResource(if (a.esterilizado) R.string.si else R.string.no)),
                                if (a.esterilizado) Tono.VERDE else Tono.AMBAR,
                            )
                            EstadoBadge(stringResource(R.string.salud_valor, a.salud), Tono.TURQUESA)
                        }
                        BotonPrincipal(stringResource(R.string.ver_perfil), { onNavegar(Rutas.conId(Rutas.ADOPCION_DETALLE, a.id)) }, color = Morado)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun AdopcionesPreview() {
    PetsafeTheme { AdopcionesContenido(DatosDemo.adopciones, {}, {}) }
}
