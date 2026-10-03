package com.so.petsafe.ui.alertas

import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.Alerta
import com.so.petsafe.domain.modelo.CategoriaAlerta
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class FiltroAlerta(@StringRes val etiqueta: Int, val categoria: CategoriaAlerta?) {
    TODAS(R.string.filtro_todas, null),
    SALUD(R.string.filtro_salud, CategoriaAlerta.SALUD),
    PERDIDOS(R.string.filtro_perdidos, CategoriaAlerta.PERDIDOS),
    CASOS(R.string.filtro_casos, CategoriaAlerta.CASOS),
    COMUNIDAD(R.string.filtro_comunidad, CategoriaAlerta.COMUNIDAD),
}

class AlertasViewModel : ViewModel() {
    private val _filtro = MutableStateFlow(FiltroAlerta.TODAS)
    val filtro = _filtro.asStateFlow()
    fun filtrar(f: FiltroAlerta) { _filtro.value = f }
}

@Composable
fun AlertasScreen(onNavegar: (String) -> Unit, vm: AlertasViewModel = viewModel()) {
    val filtro by vm.filtro.collectAsStateWithLifecycle()
    val alertas = DatosDemo.alertas.filter { filtro.categoria == null || it.categoria == filtro.categoria }
    AlertasContenido(alertas, filtro, vm::filtrar, onNavegar)
}

@Composable
fun AlertasContenido(alertas: List<Alerta>, filtro: FiltroAlerta, onFiltro: (FiltroAlerta) -> Unit, onNavegar: (String) -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(GrisSuave),
        contentPadding = PaddingValues(bottom = Dimens.EspacioXxl),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        item(key = "encabezado") { EncabezadoSeccion(stringResource(R.string.nav_alertas), stringResource(R.string.alertas_sub)) }
        item(key = "filtros") {
            LazyRow(
                contentPadding = PaddingValues(horizontal = Dimens.EspacioL),
                horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioS),
            ) {
                items(FiltroAlerta.entries, key = { it.name }) { f -> ChipFiltro(stringResource(f.etiqueta), f == filtro, { onFiltro(f) }) }
            }
        }
        items(alertas, key = { it.id }) { a ->
            TarjetaBase(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.EspacioL),
                onClick = { onNavegar(a.destino) },
            ) {
                Row(Modifier.padding(Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
                    CajaEmoji(a.emoji, fondoTono(a.tono))
                    Spacer(Modifier.width(Dimens.EspacioM))
                    Column(Modifier.weight(1f)) {
                        Text(a.titulo, fontWeight = FontWeight.Bold, color = textoTono(a.tono))
                        Text(a.descripcion, style = MaterialTheme.typography.bodySmall, color = TextoPrincipal)
                        Text(a.tiempo, style = MaterialTheme.typography.labelSmall, color = TextoSecundario)
                    }
                }
            }
        }
        if (alertas.isEmpty()) item(key = "vacio") {
            Text(stringResource(R.string.sin_resultados), color = TextoSecundario, modifier = Modifier.padding(horizontal = Dimens.EspacioL))
        }
    }
}

@Preview(showBackground = true, heightDp = 1100)
@Composable
private fun AlertasPreview() {
    PetsafeTheme { AlertasContenido(DatosDemo.alertas, FiltroAlerta.TODAS, {}, {}) }
}
