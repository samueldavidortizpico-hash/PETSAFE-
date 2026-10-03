package com.so.petsafe.ui.animales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Nfc
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Animal
import com.so.petsafe.domain.modelo.RegistroClinico
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

private val ACCESOS_CLINICOS = listOf(
    R.string.emoji_jeringa to R.string.hist_vacunas,
    R.string.emoji_microbio to R.string.hist_enfermedades,
    R.string.emoji_pastilla to R.string.hist_tratamientos,
    R.string.emoji_alerta to R.string.hist_alergias,
    R.string.emoji_hospital to R.string.hist_casos,
    R.string.emoji_grafica to R.string.hist_peso_controles,
)

@Composable
fun PerfilAnimalScreen(id: String, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    val animal = DatosDemo.animal(id)
    if (animal == null) PantallaProximamente(stringResource(R.string.perfil_animal), onVolver)
    else PerfilAnimalContenido(animal, DatosDemo.ultimosRegistros, onNavegar, onVolver)
}

@Composable
fun PerfilAnimalContenido(animal: Animal, registros: List<RegistroClinico>, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    val historial = Rutas.conId(Rutas.HISTORIAL, animal.id)
    val vacio = stringResource(R.string.sin_dato)
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.perfil_animal), onVolver) }, containerColor = GrisSuave) { p ->
        Column(
            Modifier
                .padding(p)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens.EspacioXs)) {
                AvatarAnimal(animal.especie.emoji, animal.nombre, tamano = Dimens.AvatarGrande, fondo = fondoTono(animal.estado.tono))
                Text(animal.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(stringResource(R.string.formato_punto, animal.tipo, animal.raza), color = TextoSecundario)
                Text(stringResource(R.string.formato_punto, animal.sexo, animal.edad), color = TextoSecundario)
                EstadoBadge(etiquetaEstado(animal.estado), animal.estado.tono)
            }
            TarjetaBase(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Dimens.EspacioL)) {
                    FilaDato(stringResource(R.string.dato_peso), animal.peso.ifBlank { vacio })
                    FilaDato(stringResource(R.string.dato_edad), animal.edad.ifBlank { vacio })
                    FilaDato(stringResource(R.string.dato_raza), animal.raza.ifBlank { vacio })
                    FilaDato(stringResource(R.string.dato_sexo), animal.sexo)
                    FilaDato(stringResource(R.string.dato_esterilizado), stringResource(if (animal.esterilizado) R.string.si else R.string.no))
                    FilaDato(stringResource(R.string.dato_chip), animal.chip.ifBlank { vacio })
                    FilaDato(stringResource(R.string.dato_responsable), animal.responsable)
                }
            }
            Text(stringResource(R.string.historial_clinico_mayus), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = TextoSecundario)
            ACCESOS_CLINICOS.chunked(3).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                    fila.forEach { (emoji, titulo) ->
                        TarjetaBase(Modifier.weight(1f), onClick = { onNavegar(historial) }) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(Dimens.EspacioM),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(stringResource(emoji), fontSize = Dimens.EmojiMediano)
                                Text(stringResource(titulo), style = MaterialTheme.typography.labelMedium, color = TextoPrincipal, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
            TituloSeccion(stringResource(R.string.ultimos_registros), accion = stringResource(R.string.ver_todo), onAccion = { onNavegar(historial) })
            TarjetaBase(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                    registros.forEach { r -> FilaDato(r.fecha, r.descripcion) }
                }
            }
            BotonPrincipal(stringResource(R.string.agregar_registro_clinico), { onNavegar(Rutas.NUEVO_REGISTRO) })
            BotonSecundario(stringResource(R.string.transferir_responsable), { onNavegar(Rutas.conId(Rutas.TRANSFERIR, animal.id)) })
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
                BotonSecundario(
                    stringResource(R.string.nfc_titulo), { onNavegar(Rutas.conId(Rutas.NFC, animal.id)) },
                    Modifier.weight(1f), icono = Icons.Rounded.Nfc,
                )
                BotonSecundario(
                    stringResource(R.string.marcar_perdido), { onNavegar(Rutas.conId(Rutas.MASCOTA_PERDIDA, animal.id)) },
                    Modifier.weight(1f), color = Rojo, icono = Icons.Rounded.Report,
                )
            }
            if (animal.estado.tono == Tono.ROJO) Aviso(stringResource(R.string.aviso_perdido_activo), Tono.ROJO)
        }
    }
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
private fun PerfilAnimalPreview() {
    PetsafeTheme { PerfilAnimalContenido(DatosDemo.misAnimales.first(), DatosDemo.ultimosRegistros, {}, {}) }
}
