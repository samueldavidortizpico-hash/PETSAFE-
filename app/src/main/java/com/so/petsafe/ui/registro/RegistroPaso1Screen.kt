package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.so.petsafe.R
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun RegistroPaso1Screen(vm: RegistroViewModel, onContinuar: () -> Unit, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.registro_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        RegistroPaso1Contenido(
            estado, vm::actualizar, onContinuar,
            Modifier
                .padding(p)
                .consumeWindowInsets(p),
        )
    }
}

@Composable
fun RegistroPaso1Contenido(
    estado: RegistroEstado,
    onCambio: ((RegistroEstado) -> RegistroEstado) -> Unit,
    onContinuar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val req = stringResource(R.string.error_obligatorio)
    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioL),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
    ) {
        ProgresoRegistro(1)
        Text(stringResource(R.string.paso1_titulo), style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
        CampoTextoConError(
            estado.nombre, { v -> onCambio { it.copy(nombre = v) } },
            stringResource(R.string.campo_nombre_animal), if (estado.nombre.isBlank()) req else null, obligatorio = true,
        )
        SelectorOpciones(
            stringResource(R.string.campo_especie), stringArrayResource(R.array.especies_registro).toList(),
            estado.especie, { v -> onCambio { it.copy(especie = v) } },
        )
        SelectorOpciones(
            stringResource(R.string.campo_sexo), stringArrayResource(R.array.sexos).toList(),
            estado.sexo, { v -> onCambio { it.copy(sexo = v) } },
        )
        CampoTextoConError(estado.raza, { v -> onCambio { it.copy(raza = v) } }, stringResource(R.string.dato_raza), null)
        CampoTextoConError(estado.color, { v -> onCambio { it.copy(color = v) } }, stringResource(R.string.campo_color), null)
        CampoTextoConError(estado.edad, { v -> onCambio { it.copy(edad = v) } }, stringResource(R.string.campo_edad_nacimiento), null)
        CampoTextoConError(
            estado.peso, { v -> onCambio { it.copy(peso = v) } }, stringResource(R.string.campo_peso),
            if (estado.pesoValido) null else stringResource(R.string.error_numero), teclado = KeyboardType.Decimal,
        )
        BotonAdjunto(stringResource(R.string.agregar_foto), estado.foto, { onCambio { it.copy(foto = true) } })
        BotonPrincipal(stringResource(R.string.continuar), onContinuar, habilitado = estado.paso1Valido)
    }
}

@Preview(showBackground = true, heightDp = 1100)
@Composable
private fun Paso1Preview() {
    PetsafeTheme { RegistroPaso1Contenido(RegistroEstado(nombre = "Toby"), {}, {}) }
}
