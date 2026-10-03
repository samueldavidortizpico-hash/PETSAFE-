package com.so.petsafe.ui.animales

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Animal
import com.so.petsafe.domain.modelo.Especie
import com.so.petsafe.domain.modelo.EstadoAnimal
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class FiltroAnimal(@StringRes val etiqueta: Int) {
    TODOS(R.string.filtro_todos), PERROS(R.string.filtro_perros), GATOS(R.string.filtro_gatos), ATENCION(R.string.filtro_atencion);

    fun acepta(a: Animal) = when (this) {
        TODOS -> true
        PERROS -> a.especie == Especie.PERRO
        GATOS -> a.especie == Especie.GATO
        ATENCION -> a.estado != EstadoAnimal.ACTIVO
    }
}

class MisAnimalesViewModel : ViewModel() {
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()
    private val _filtro = MutableStateFlow(FiltroAnimal.TODOS)
    val filtro = _filtro.asStateFlow()
    fun buscar(texto: String) { _busqueda.value = texto }
    fun filtrar(f: FiltroAnimal) { _filtro.value = f }
}

@Composable
fun MisAnimalesScreen(onNavegar: (String) -> Unit, onVolver: () -> Unit, vm: MisAnimalesViewModel = viewModel()) {
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val filtro by vm.filtro.collectAsStateWithLifecycle()
    val animales = DatosDemo.misAnimales.filter {
        filtro.acepta(it) && (busqueda.isBlank() || listOf(it.nombre, it.raza, it.chip, it.responsable).any { c -> c.contains(busqueda.trim(), ignoreCase = true) })
    }
    MisAnimalesContenido(animales, busqueda, vm::buscar, filtro, vm::filtrar, onNavegar, onVolver)
}

@Composable
fun MisAnimalesContenido(
    animales: List<Animal>,
    busqueda: String,
    onBuscar: (String) -> Unit,
    filtro: FiltroAnimal,
    onFiltro: (FiltroAnimal) -> Unit,
    onNavegar: (String) -> Unit,
    onVolver: () -> Unit,
) {
    Scaffold(
        topBar = { EncabezadoVolver(stringResource(R.string.mis_animales), onVolver) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavegar(Rutas.REGISTRAR) }, containerColor = Turquesa, contentColor = Blanco) {
                Icon(Icons.Rounded.Add, stringResource(R.string.accion_registrar))
            }
        },
        containerColor = GrisSuave,
    ) { p ->
        LazyColumn(
            Modifier
                .padding(p)
                .consumeWindowInsets(p)
                .imePadding(),
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            item(key = "buscador") { BuscadorBar(busqueda, onBuscar, stringResource(R.string.buscar_animal)) }
            item(key = "filtros") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                    items(FiltroAnimal.entries, key = { it.name }) { f -> ChipFiltro(stringResource(f.etiqueta), f == filtro, { onFiltro(f) }) }
                }
            }
            items(animales, key = { it.id }) { a ->
                TarjetaBase(Modifier.fillMaxWidth(), onClick = { onNavegar(Rutas.conId(Rutas.PERFIL_ANIMAL, a.id)) }) {
                    Row(Modifier.padding(Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
                        AvatarAnimal(a.especie.emoji, a.tipo, tamano = Dimens.CajaIconoGrande, fondo = fondoTono(a.estado.tono))
                        Spacer(Modifier.width(Dimens.EspacioM))
                        Column(Modifier.weight(1f)) {
                            Text(a.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                            Text(stringResource(R.string.formato_punto, a.tipo, a.raza), style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                            Text(stringResource(R.string.formato_punto, a.sexo, a.edad), style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                        }
                        EstadoBadge(etiquetaEstado(a.estado), a.estado.tono)
                    }
                }
            }
            if (animales.isEmpty()) item(key = "vacio") { Text(stringResource(R.string.sin_resultados), color = TextoSecundario) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MisAnimalesPreview() {
    PetsafeTheme { MisAnimalesContenido(DatosDemo.misAnimales.toList(), "", {}, FiltroAnimal.TODOS, {}, {}, {}) }
}
