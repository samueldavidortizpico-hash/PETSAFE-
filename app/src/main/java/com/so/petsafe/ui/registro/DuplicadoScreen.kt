package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Coincidencia
import com.so.petsafe.domain.modelo.Especie
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun DuplicadoScreen(vm: RegistroViewModel, onRevisar: (String) -> Unit, onGuardado: (String) -> Unit, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val especies = stringArrayResource(R.array.especies_registro)
    val si = stringResource(R.string.si)
    DuplicadoContenido(
        coincidencias = DatosDemo.coincidencias,
        onRevisar = onRevisar,
        onContinuar = {
            val especie = listOf(Especie.PERRO, Especie.GATO, Especie.OTRO).getOrElse(especies.indexOf(estado.especie)) { Especie.OTRO }
            onGuardado(vm.guardar(especie, esterilizado = estado.esterilizado == si))
        },
        onVolver = onVolver,
    )
}

@Composable
fun DuplicadoContenido(coincidencias: List<Coincidencia>, onRevisar: (String) -> Unit, onContinuar: () -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.duplicado_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        LazyColumn(
            Modifier.padding(p),
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            item(key = "aviso") {
                Aviso(stringResource(R.string.duplicado_texto), Tono.AMBAR, icono = Icons.Rounded.ContentCopy)
            }
            items(coincidencias, key = { it.id }) { c ->
                TarjetaBase(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarAnimal(Especie.PERRO.emoji, c.nombre)
                            Spacer(Modifier.width(Dimens.EspacioM))
                            Column(Modifier.weight(1f)) {
                                Text(c.nombre, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                                Text(c.descripcion, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                            }
                            EstadoBadge(
                                stringResource(R.string.coincidencia_pct, c.porcentaje),
                                if (c.porcentaje >= 85) Tono.ROJO else Tono.AMBAR,
                            )
                        }
                        BotonSecundario(stringResource(R.string.revisar_coincidencia), { onRevisar(c.id) })
                    }
                }
            }
            item(key = "continuar") {
                BotonPrincipal(stringResource(R.string.es_otro_animal), onContinuar)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DuplicadoPreview() {
    PetsafeTheme { DuplicadoContenido(DatosDemo.coincidencias, {}, {}, {}) }
}
