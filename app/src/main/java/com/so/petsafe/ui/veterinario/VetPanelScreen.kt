package com.so.petsafe.ui.veterinario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Paciente
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun VetPanelScreen(onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    val pacientes = DatosDemo.pacientes.filter {
        busqueda.isBlank() || it.nombre.contains(busqueda.trim(), true) || it.detalle.contains(busqueda.trim(), true) || it.id.contains(busqueda.trim(), true)
    }
    VetPanelContenido(pacientes, busqueda, { busqueda = it }, onNavegar, onVolver)
}

@Composable
fun VetPanelContenido(pacientes: List<Paciente>, busqueda: String, onBuscar: (String) -> Unit, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.modo_veterinario), onVolver) }, containerColor = GrisSuave) { p ->
        LazyColumn(
            Modifier
                .padding(p)
                .consumeWindowInsets(p)
                .imePadding(),
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            item(key = "vet") {
                TarjetaBase(Modifier.fillMaxWidth(), color = TurquesaOscuro) {
                    Column(Modifier.padding(Dimens.EspacioL)) {
                        Text(stringResource(R.string.modo_veterinario), color = BlancoSuave, style = MaterialTheme.typography.labelLarge)
                        Text(stringResource(R.string.vet_nombre), color = Blanco, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.vet_clinica), color = BlancoSuave)
                    }
                }
            }
            item(key = "buscar") { BuscadorBar(busqueda, onBuscar, stringResource(R.string.vet_buscar)) }
            item(key = "contadores") {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
                    Contador(stringResource(R.string.vet_num_pacientes), stringResource(R.string.vet_pacientes), Modifier.weight(1f))
                    Contador(stringResource(R.string.vet_num_hoy), stringResource(R.string.vet_hoy), Modifier.weight(1f))
                    Contador(stringResource(R.string.vet_num_pendientes), stringResource(R.string.vet_pendientes), Modifier.weight(1f))
                }
            }
            item(key = "titulo") { TituloSeccion(stringResource(R.string.vet_recientes)) }
            items(pacientes, key = { it.id }) { pac ->
                TarjetaBase(Modifier.fillMaxWidth(), onClick = { onNavegar(Rutas.conId(Rutas.VET_HISTORIAL, pac.id)) }) {
                    Row(Modifier.padding(Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
                        AvatarAnimal(pac.especie.emoji, pac.nombre)
                        Spacer(Modifier.width(Dimens.EspacioM))
                        Column(Modifier.weight(1f)) {
                            Text(pac.nombre, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                            Text(pac.detalle, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                        }
                    }
                }
            }
            item(key = "nuevo") { BotonPrincipal(stringResource(R.string.nuevo_registro_clinico), { onNavegar(Rutas.VET_NUEVO) }) }
        }
    }
}

@Composable
private fun Contador(numero: String, etiqueta: String, modifier: Modifier) {
    TarjetaBase(modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.EspacioM),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(numero, fontSize = Dimens.NumeroGrande, fontWeight = FontWeight.Bold, color = Turquesa)
            Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = TextoSecundario, textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun VetPanelPreview() {
    PetsafeTheme { VetPanelContenido(DatosDemo.pacientes, "", {}, {}, {}) }
}
