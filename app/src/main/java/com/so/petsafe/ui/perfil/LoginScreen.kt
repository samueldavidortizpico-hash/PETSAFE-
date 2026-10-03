package com.so.petsafe.ui.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.SesionFalsa
import com.so.petsafe.domain.Validaciones
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

const val MIN_CONTRASENA = 6

// Por ahora activa SesionFalsa; la autenticación real llega con Firebase.
@Composable
fun LoginScreen(onNavegar: (String) -> Unit, onSesionIniciada: () -> Unit, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.iniciar_sesion), onVolver) }, containerColor = GrisSuave) { p ->
        LoginContenido(
            onEntrar = { SesionFalsa.haySesion = true; onSesionIniciada() },
            onNavegar = onNavegar,
            modifier = Modifier
                .padding(p)
                .consumeWindowInsets(p),
        )
    }
}

@Composable
fun LoginContenido(onEntrar: () -> Unit, onNavegar: (String) -> Unit, modifier: Modifier = Modifier) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    val errorCorreo = if (Validaciones.esCorreo(correo)) null else stringResource(R.string.error_correo)
    val errorContrasena = if (contrasena.length < MIN_CONTRASENA) stringResource(R.string.error_contrasena, MIN_CONTRASENA) else null
    val olvide = stringResource(R.string.olvide_contrasena)

    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.EspacioXxl),
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AvatarAnimal(stringResource(R.string.emoji_huella), stringResource(R.string.app_nombre), tamano = Dimens.AvatarGrande)
        Text(stringResource(R.string.bienvenida), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        CampoTextoConError(correo, { correo = it.trim() }, stringResource(R.string.campo_correo), errorCorreo, obligatorio = true, teclado = KeyboardType.Email)
        CampoTextoConError(contrasena, { contrasena = it }, stringResource(R.string.campo_contrasena), errorContrasena, obligatorio = true, teclado = KeyboardType.Password, esContrasena = true)
        BotonPrincipal(stringResource(R.string.iniciar_sesion), onEntrar, habilitado = errorCorreo == null && errorContrasena == null)
        TextButton(onClick = { onNavegar(Rutas.proximamente(olvide)) }) { Text(olvide, color = Turquesa) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.sin_cuenta), color = TextoSecundario)
            TextButton(onClick = { onNavegar(Rutas.CREAR_CUENTA) }) { Text(stringResource(R.string.crear_cuenta), color = Turquesa, fontWeight = FontWeight.Bold) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    PetsafeTheme { LoginContenido({}, {}) }
}
