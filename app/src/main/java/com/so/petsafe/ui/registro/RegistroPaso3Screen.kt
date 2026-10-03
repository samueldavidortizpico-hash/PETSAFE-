package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.so.petsafe.R
import com.so.petsafe.domain.Validaciones
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun RegistroPaso3Screen(vm: RegistroViewModel, onContinuar: () -> Unit, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.registro_titulo), onVolver) }, containerColor = GrisSuave) { p ->
        RegistroPaso3Contenido(
            estado, vm::actualizar, onContinuar, onVolver,
            Modifier
                .padding(p)
                .consumeWindowInsets(p),
        )
    }
}

@Composable
fun RegistroPaso3Contenido(
    estado: RegistroEstado,
    onCambio: ((RegistroEstado) -> RegistroEstado) -> Unit,
    onRegistrar: () -> Unit,
    onAnterior: () -> Unit,
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
        ProgresoRegistro(3)
        Text(stringResource(R.string.paso3_titulo), style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
        CampoTextoConError(
            estado.propietario, { v -> onCambio { it.copy(propietario = v) } },
            stringResource(R.string.campo_nombre_completo), if (estado.propietario.isBlank()) req else null, obligatorio = true,
        )
        CampoTextoConError(
            estado.documento, { v -> onCambio { it.copy(documento = v) } },
            stringResource(R.string.campo_documento), if (estado.documentoValido) null else stringResource(R.string.error_documento),
            obligatorio = true, teclado = KeyboardType.Number,
        )
        CampoTextoConError(
            estado.telefono, { v -> onCambio { it.copy(telefono = v) } },
            stringResource(R.string.campo_telefono), if (Validaciones.esTelefono(estado.telefono)) null else stringResource(R.string.error_telefono),
            obligatorio = true, teclado = KeyboardType.Phone,
        )
        CampoTextoConError(
            estado.correo, { v -> onCambio { it.copy(correo = v) } },
            stringResource(R.string.campo_correo), if (Validaciones.esCorreo(estado.correo)) null else stringResource(R.string.error_correo),
            obligatorio = true, teclado = KeyboardType.Email,
        )
        CasillaVerificacion(stringResource(R.string.autorizo_datos), estado.autoriza, { v -> onCambio { it.copy(autoriza = v) } })
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
            BotonSecundario(stringResource(R.string.anterior), onAnterior, Modifier.weight(1f))
            BotonPrincipal(stringResource(R.string.registrar_animal), onRegistrar, Modifier.weight(1f), habilitado = estado.paso3Valido)
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun Paso3Preview() {
    PetsafeTheme { RegistroPaso3Contenido(RegistroEstado(), {}, {}, {}) }
}
