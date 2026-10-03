package com.so.petsafe.ui.animales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.modelo.RegistroClinico
import com.so.petsafe.domain.modelo.SeccionHistorial
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

/** Usado por el responsable (historial-clinico) y por el veterinario (vet-historial). */
@Composable
fun HistorialClinicoScreen(id: String, onAgregar: () -> Unit, onVolver: () -> Unit) {
    val nombre = DatosDemo.animal(id)?.nombre ?: DatosDemo.pacientes.firstOrNull { it.id == id }?.nombre.orEmpty()
    HistorialContenido(nombre, DatosDemo.historial, onAgregar, onVolver)
}

@Composable
fun HistorialContenido(nombre: String, secciones: List<SeccionHistorial>, onAgregar: () -> Unit, onVolver: () -> Unit) {
    Scaffold(
        topBar = {
            EncabezadoVolver(
                if (nombre.isBlank()) stringResource(R.string.historial_clinico) else stringResource(R.string.historial_de, nombre),
                onVolver,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAgregar,
                containerColor = Turquesa,
                contentColor = Blanco,
                icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.agregar_registro)) },
            )
        },
        containerColor = GrisSuave,
    ) { p ->
        LazyColumn(
            Modifier.padding(p),
            contentPadding = PaddingValues(start = Dimens.EspacioL, end = Dimens.EspacioL, top = Dimens.EspacioL, bottom = Dimens.AvatarGrande),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioS),
        ) {
            secciones.forEach { s ->
                item(key = "t-${s.titulo}") {
                    Text(
                        s.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                        modifier = Modifier.padding(top = Dimens.EspacioS),
                    )
                }
                if (s.registros.isEmpty()) item(key = "v-${s.titulo}") {
                    Text(stringResource(R.string.sin_registros), color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                }
                itemsIndexed(s.registros, key = { i, _ -> "${s.titulo}-$i" }) { _, r -> TarjetaRegistro(r) }
            }
        }
    }
}

@Composable
private fun TarjetaRegistro(r: RegistroClinico) {
    TarjetaBase(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioXs)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.formato_punto, r.fecha, r.tipo), style = MaterialTheme.typography.labelMedium, color = TextoSecundario, modifier = Modifier.weight(1f))
                EstadoBadge(r.estado, Tono.TURQUESA)
            }
            Text(r.descripcion, fontWeight = FontWeight.SemiBold, color = TextoPrincipal)
            if (r.verificado) Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Verified, contentDescription = null, tint = Verde, modifier = Modifier.size(Dimens.IconoPequeno))
                Spacer(Modifier.width(Dimens.EspacioXs))
                Text(stringResource(R.string.verificado_vet), style = MaterialTheme.typography.labelSmall, color = VerdeOscuro)
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun HistorialPreview() {
    PetsafeTheme { HistorialContenido("Toby", DatosDemo.historial, {}, {}) }
}
