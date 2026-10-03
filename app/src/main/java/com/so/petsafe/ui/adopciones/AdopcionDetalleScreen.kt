package com.so.petsafe.ui.adopciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.*
import com.so.petsafe.domain.modelo.*
import com.so.petsafe.navigation.Rutas
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

@Composable
fun AdopcionDetalleScreen(id: String, onNavegar: (String) -> Unit, onVolver: () -> Unit) {
    val animal = DatosDemo.adopcion(id)
    if (animal == null) {
        PantallaProximamente(stringResource(R.string.com_adopciones), onVolver)
        return
    }
    AdopcionDetalleContenido(animal, onAdoptar = { onNavegar(Rutas.conId(Rutas.SOLICITUD_ADOPCION, id)) }, onVolver = onVolver)
}

@Composable
fun AdopcionDetalleContenido(animal: AnimalAdopcion, onAdoptar: () -> Unit, onVolver: () -> Unit) {
    Scaffold(
        topBar = { EncabezadoVolver(animal.nombre, onVolver) },
        bottomBar = {
            Surface(color = Blanco, shadowElevation = Dimens.ElevacionBarra) {
                BotonPrincipal(
                    stringResource(R.string.quiero_adoptar),
                    onAdoptar,
                    Modifier
                        .navigationBarsPadding()
                        .padding(Dimens.EspacioL),
                    color = Morado,
                )
            }
        },
        containerColor = GrisSuave,
    ) { p ->
        Column(
            Modifier
                .padding(p)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AvatarAnimal(animal.especie.emoji, animal.tipo, tamano = Dimens.AvatarGrande, fondo = MoradoFondo)
                Spacer(Modifier.height(Dimens.EspacioS))
                Text(animal.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(stringResource(R.string.formato_tres, animal.tipo, animal.raza, animal.sexo), color = TextoSecundario)
            }
            TarjetaBase(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Dimens.EspacioL)) {
                    Text(animal.descripcion, color = TextoPrincipal)
                    Spacer(Modifier.height(Dimens.EspacioS))
                    FilaDato(stringResource(R.string.dato_edad), animal.edad)
                    FilaDato(stringResource(R.string.dato_raza), animal.raza)
                    FilaDato(stringResource(R.string.dato_sexo), animal.sexo)
                    FilaDato(stringResource(R.string.dato_salud), animal.salud)
                }
            }
            TituloSeccion(stringResource(R.string.info_clinica_autorizada))
            TarjetaBase(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Dimens.EspacioL)) {
                    FilaDato(stringResource(R.string.hist_vacunas), animal.vacunas.joinToString(", "))
                    FilaDato(stringResource(R.string.hist_enfermedades), stringResource(R.string.ninguna_registrada))
                    FilaDato(stringResource(R.string.hist_alergias), stringResource(R.string.ninguna_conocida))
                    FilaDato(stringResource(R.string.hist_tratamientos), stringResource(R.string.ninguno_activo))
                    FilaDato(stringResource(R.string.dato_esterilizado), stringResource(if (animal.esterilizado) R.string.si else R.string.no))
                }
            }
            Aviso(stringResource(R.string.adopcion_evaluacion), Tono.TURQUESA)
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun AdopcionDetallePreview() {
    PetsafeTheme { AdopcionDetalleContenido(DatosDemo.adopciones.first(), {}, {}) }
}
