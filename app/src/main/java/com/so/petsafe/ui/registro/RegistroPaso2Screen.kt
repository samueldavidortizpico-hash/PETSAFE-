package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.so.petsafe.R
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun RegistroPaso2Screen(vm: RegistroViewModel, onContinuar: () -> Unit, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.registro_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        RegistroPaso2Contenido(
            estado, vm::actualizar, onContinuar, onVolver,
            Modifier
                .padding(p)
                .consumeWindowInsets(p),
        )
    }
}

@Composable
fun RegistroPaso2Contenido(
    estado: RegistroEstado,
    onCambio: ((RegistroEstado) -> RegistroEstado) -> Unit,
    onContinuar: () -> Unit,
    onAnterior: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val siNo = listOf(stringResource(R.string.si), stringResource(R.string.no))
    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        ProgresoRegistro(2)
        Text(stringResource(R.string.paso2_titulo), style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
        SelectorOpciones(stringResource(R.string.campo_tiene_propietario), siNo, estado.tienePropietario, { v -> onCambio { it.copy(tienePropietario = v) } })
        SelectorOpciones(stringResource(R.string.campo_tiene_hogar), siNo, estado.tieneHogar, { v -> onCambio { it.copy(tieneHogar = v) } })
        SelectorOpciones(
            stringResource(R.string.campo_esterilizado),
            siNo + stringResource(R.string.no_se_sabe),
            estado.esterilizado, { v -> onCambio { it.copy(esterilizado = v) } },
        )
        SelectorOpciones(
            stringResource(R.string.campo_estado_actual),
            stringArrayResource(R.array.estados_animal).toList(),
            estado.estadoActual, { v -> onCambio { it.copy(estadoActual = v) } },
        )
        CampoTextoConError(
            estado.observaciones, { v -> onCambio { it.copy(observaciones = v) } },
            stringResource(R.string.campo_observaciones), null, lineasMin = 3,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
            BotonSecundario(stringResource(R.string.anterior), onAnterior, Modifier.weight(1f))
            BotonPrincipal(stringResource(R.string.continuar), onContinuar, Modifier.weight(1f), habilitado = estado.paso2Valido)
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun Paso2Preview() {
    PetsafeTheme { RegistroPaso2Contenido(RegistroEstado(), {}, {}, {}) }
}
