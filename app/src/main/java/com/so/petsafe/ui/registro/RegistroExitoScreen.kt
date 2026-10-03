package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun RegistroExitoScreen(id: String, onNavegar: (String) -> Unit, onReemplazar: (String) -> Unit) {
    Scaffold(containerColor = GrisSuave) { p ->
        PantallaExito(
            titulo = stringResource(R.string.registro_exito),
            mensaje = stringResource(R.string.registro_exito_msg),
            modifier = Modifier.padding(p),
        ) {
            BotonPrincipal(stringResource(R.string.ver_perfil_animal), { onNavegar(Rutas.conId(Rutas.PERFIL_ANIMAL, id)) })
            BotonSecundario(stringResource(R.string.agregar_info_clinica), { onNavegar(Rutas.NUEVO_REGISTRO) })
            BotonSecundario(stringResource(R.string.volver_inicio), { onReemplazar(Rutas.HOME) }, color = TextoSecundario)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistroExitoPreview() {
    PetsafeTheme { RegistroExitoScreen("toby", {}, {}) }
}
