package com.so.petsafe.ui.comunidad

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import com.so.petsafe.domain.*
import com.so.petsafe.domain.modelo.*
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class FiltroReporte(@StringRes val etiqueta: Int) {
    TODOS(R.string.filtro_todos), PERDIDOS(R.string.filtro_perdidos), ENCONTRADOS(R.string.filtro_encontrados)
}

class PerdidosViewModel : ViewModel() {
    private val _filtro = MutableStateFlow(FiltroReporte.TODOS)
    val filtro = _filtro.asStateFlow()
    fun filtrar(f: FiltroReporte) { _filtro.value = f }
}

@Composable
fun PerdidosEncontradosScreen(onNavegar: (String) -> Unit, onVolver: () -> Unit, vm: PerdidosViewModel = viewModel()) {
    val filtro by vm.filtro.collectAsStateWithLifecycle()
    val reportes = DatosDemo.reportes.filter {
        when (filtro) {
            FiltroReporte.TODOS -> true
            FiltroReporte.PERDIDOS -> it.perdido
            FiltroReporte.ENCONTRADOS -> !it.perdido
        }
    }
    PerdidosContenido(reportes, filtro, vm::filtrar, onNavegar, onVolver)
}

@Composable
fun PerdidosContenido(
    reportes: List<ReporteComunidad>,
    filtro: FiltroReporte,
    onFiltro: (FiltroReporte) -> Unit,
    onNavegar: (String) -> Unit,
    onVolver: () -> Unit,
) {
    var contactando by remember { mutableStateOf(false) }
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.com_perdidos), onVolver) }, containerColor = GrisSuave) { p ->
        LazyColumn(
            Modifier.padding(p),
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            item(key = "botones") {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
                    BotonPrincipal(stringResource(R.string.accion_reportar_perdido), { onNavegar(Rutas.REPORTE_PERDIDO) }, Modifier.weight(1f), color = Naranja)
                    BotonPrincipal(stringResource(R.string.reportar_encontrado), { onNavegar(Rutas.REPORTE_ENCONTRADO) }, Modifier.weight(1f), color = Verde)
                }
            }
            item(key = "filtros") {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                    FiltroReporte.entries.forEach { f -> ChipFiltro(stringResource(f.etiqueta), f == filtro, { onFiltro(f) }) }
                }
            }
            items(reportes, key = { it.id }) { TarjetaReporte(it) { contactando = true } }
            if (reportes.isEmpty()) item(key = "vacio") {
                Text(stringResource(R.string.sin_resultados), color = TextoSecundario)
            }
        }
    }
    if (contactando) DialogoContacto { contactando = false }
}

@Composable
private fun TarjetaReporte(r: ReporteComunidad, onContactar: () -> Unit) {
    val tono = if (r.perdido) Tono.ROJO else Tono.VERDE
    TarjetaBase(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarAnimal(r.especie.emoji, r.raza, fondo = fondoTono(tono))
                Spacer(Modifier.width(Dimens.EspacioM))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioXs)) {
                    EstadoBadge(stringResource(if (r.perdido) R.string.badge_perdido else R.string.badge_encontrado), tono)
                    val nombre = r.nombre ?: stringResource(R.string.sin_nombre)
                    Text(
                        if (r.raza.isBlank()) nombre else stringResource(R.string.formato_punto, nombre, r.raza),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                    )
                    Text(stringResource(R.string.formato_ubicacion, r.zona, r.fecha), style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                }
            }
            Text(r.descripcion, style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
            BotonSecundario(stringResource(R.string.ver_detalles_contactar), onContactar)
        }
    }
}

@Composable
private fun DialogoContacto(onCerrar: () -> Unit) {
    var enviado by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(stringResource(R.string.contacto_titulo)) },
        text = { Text(stringResource(if (enviado) R.string.contacto_enviado else R.string.contacto_texto)) },
        confirmButton = {
            if (enviado) TextButton(onClick = onCerrar) { Text(stringResource(R.string.cerrar)) }
            else TextButton(onClick = { enviado = true }) { Text(stringResource(R.string.contacto_enviar)) }
        },
        dismissButton = { if (!enviado) TextButton(onClick = onCerrar) { Text(stringResource(R.string.cancelar)) } },
    )
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun PerdidosPreview() {
    PetsafeTheme { PerdidosContenido(DatosDemo.reportes.toList(), FiltroReporte.TODOS, {}, {}, {}) }
}
