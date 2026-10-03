package com.so.petsafe.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.so.petsafe.R
import com.so.petsafe.ui.adopciones.AdopcionDetalleScreen
import com.so.petsafe.ui.adopciones.AdopcionEnviadaScreen
import com.so.petsafe.ui.adopciones.AdopcionesScreen
import com.so.petsafe.ui.adopciones.SolicitudAdopcionScreen
import com.so.petsafe.ui.alertas.AlertasScreen
import com.so.petsafe.ui.animales.HistorialClinicoScreen
import com.so.petsafe.ui.animales.MascotaPerdidaScreen
import com.so.petsafe.ui.animales.MisAnimalesScreen
import com.so.petsafe.ui.animales.NfcScreen
import com.so.petsafe.ui.animales.PerfilAnimalScreen
import com.so.petsafe.ui.animales.TransferirScreen
import com.so.petsafe.ui.ayuda.ArticuloScreen
import com.so.petsafe.ui.ayuda.CentroAyudaScreen
import com.so.petsafe.ui.ayuda.ChatbotScreen
import com.so.petsafe.ui.componentes.PantallaProximamente
import com.so.petsafe.ui.comunidad.ComunidadScreen
import com.so.petsafe.ui.comunidad.PerdidosEncontradosScreen
import com.so.petsafe.ui.comunidad.ReporteScreen
import com.so.petsafe.ui.denuncias.DenunciaEnviadaScreen
import com.so.petsafe.ui.denuncias.DenunciaScreen
import com.so.petsafe.ui.denuncias.SeguimientoCasoScreen
import com.so.petsafe.ui.home.HomeScreen
import com.so.petsafe.ui.municipal.IndicadoresMunicipalesScreen
import com.so.petsafe.ui.perfil.CrearCuentaScreen
import com.so.petsafe.ui.perfil.LoginScreen
import com.so.petsafe.ui.perfil.PerfilScreen
import com.so.petsafe.ui.registro.DuplicadoScreen
import com.so.petsafe.ui.registro.RegistrarSituacionScreen
import com.so.petsafe.ui.registro.RegistroEncontradoScreen
import com.so.petsafe.ui.registro.RegistroExitoScreen
import com.so.petsafe.ui.registro.RegistroPaso1Screen
import com.so.petsafe.ui.registro.RegistroPaso2Screen
import com.so.petsafe.ui.registro.RegistroPaso3Screen
import com.so.petsafe.ui.registro.RegistroViewModel
import com.so.petsafe.ui.theme.*
import com.so.petsafe.ui.urgencias.UrgenciasScreen
import com.so.petsafe.ui.veterinario.NuevoRegistroClinicoScreen
import com.so.petsafe.ui.veterinario.VetPanelScreen

private val PESTANAS = listOf(Rutas.HOME, Rutas.COMUNIDAD, Rutas.ALERTAS, Rutas.PERFIL)

private fun NavBackStackEntry.id() = arguments?.getString("id").orEmpty()

private fun NavController.irPestana(ruta: String) = navigate(ruta) {
    popUpTo(graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
}

@Composable
fun NavegacionApp() {
    val nav = rememberNavController()
    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    val ir: (String) -> Unit = { nav.navigate(it) { launchSingleTop = true } }
    // Evita doble "volver" que dejaría la pila vacía.
    val volver: () -> Unit = { if (nav.previousBackStackEntry != null) nav.popBackStack() }
    // Reemplaza el flujo actual: vuelve a Inicio y abre la ruta indicada encima.
    val reemplazar: (String) -> Unit = { ruta ->
        nav.navigate(ruta) {
            popUpTo(Rutas.HOME) { inclusive = ruta == Rutas.HOME }
            launchSingleTop = true
        }
    }

    Scaffold(
        containerColor = GrisSuave,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (rutaActual in PESTANAS) {
                BarraInferior(rutaActual, onPestana = { nav.irPestana(it) }, onRegistrar = { ir(Rutas.REGISTRAR) })
            }
        },
    ) { innerPadding ->
        NavHost(nav, startDestination = Rutas.HOME, modifier = Modifier.padding(innerPadding)) {
            // Las 4 secciones con barra inferior
            composable(Rutas.HOME) { HomeScreen(ir) }
            composable(Rutas.COMUNIDAD) { ComunidadScreen(ir) }
            composable(Rutas.ALERTAS) { AlertasScreen(ir) }
            composable(Rutas.PERFIL) { PerfilScreen(ir) }

            // Comunidad
            composable(Rutas.PERDIDOS) { PerdidosEncontradosScreen(ir, volver) }
            composable(Rutas.REPORTE_PERDIDO) { ReporteScreen(esPerdido = true, onReemplazar = reemplazar, onVolver = volver) }
            composable(Rutas.REPORTE_ENCONTRADO) { ReporteScreen(esPerdido = false, onReemplazar = reemplazar, onVolver = volver) }

            // Adopciones
            composable(Rutas.ADOPCIONES) { AdopcionesScreen(ir, volver) }
            composable("${Rutas.ADOPCION_DETALLE}/{id}") { AdopcionDetalleScreen(it.id(), ir, volver) }
            composable("${Rutas.SOLICITUD_ADOPCION}/{id}") {
                SolicitudAdopcionScreen(it.id(), onEnviada = { reemplazar(Rutas.ADOPCION_ENVIADA) }, onVolver = volver)
            }
            composable(Rutas.ADOPCION_ENVIADA) { AdopcionEnviadaScreen(reemplazar) }

            // Registro de animales (3 pasos con ViewModel compartido)
            composable(Rutas.REGISTRAR) { RegistrarSituacionScreen(ir, volver) }
            composable(Rutas.PASO1) { e ->
                val vm: RegistroViewModel = viewModel(e)
                RegistroPaso1Screen(vm, onContinuar = { ir(Rutas.PASO2) }, onVolver = volver)
            }
            composable(Rutas.PASO2) { e ->
                val padre = remember(e) { nav.getBackStackEntry(Rutas.PASO1) }
                RegistroPaso2Screen(viewModel(padre), onContinuar = { ir(Rutas.PASO3) }, onVolver = volver)
            }
            composable(Rutas.PASO3) { e ->
                val padre = remember(e) { nav.getBackStackEntry(Rutas.PASO1) }
                RegistroPaso3Screen(viewModel(padre), onContinuar = { ir(Rutas.DUPLICADO) }, onVolver = volver)
            }
            composable(Rutas.DUPLICADO) { e ->
                val padre = remember(e) { nav.getBackStackEntry(Rutas.PASO1) }
                DuplicadoScreen(
                    viewModel(padre),
                    onRevisar = { ir(Rutas.conId(Rutas.PERFIL_ANIMAL, it)) },
                    onGuardado = { id -> reemplazar(Rutas.conId(Rutas.REGISTRO_EXITO, id)) },
                    onVolver = volver,
                )
            }
            composable("${Rutas.REGISTRO_EXITO}/{id}") { RegistroExitoScreen(it.id(), ir, reemplazar) }
            composable(Rutas.REGISTRO_ENCONTRADO) { RegistroEncontradoScreen(reemplazar, volver) }

            // Mis animales
            composable(Rutas.MIS_ANIMALES) { MisAnimalesScreen(ir, volver) }
            composable("${Rutas.PERFIL_ANIMAL}/{id}") { PerfilAnimalScreen(it.id(), ir, volver) }
            composable("${Rutas.HISTORIAL}/{id}") { HistorialClinicoScreen(it.id(), onAgregar = { ir(Rutas.NUEVO_REGISTRO) }, onVolver = volver) }
            composable("${Rutas.NFC}/{id}") { NfcScreen(it.id(), volver) }
            composable("${Rutas.TRANSFERIR}/{id}") { TransferirScreen(it.id(), volver) }
            composable("${Rutas.MASCOTA_PERDIDA}/{id}") { MascotaPerdidaScreen(it.id(), volver) }
            composable(Rutas.NUEVO_REGISTRO) { NuevoRegistroClinicoScreen(esVeterinario = false, onVolver = volver) }

            // Urgencias y denuncias
            composable(Rutas.URGENCIAS) { UrgenciasScreen(volver) }
            composable(Rutas.DENUNCIA) { DenunciaScreen(onEnviada = { reemplazar(Rutas.DENUNCIA_ENVIADA) }, onVolver = volver) }
            composable(Rutas.DENUNCIA_ENVIADA) { DenunciaEnviadaScreen(reemplazar) }
            composable(Rutas.SEGUIMIENTO) { SeguimientoCasoScreen(volver) }

            // Ayuda
            composable(Rutas.CENTRO_AYUDA) { CentroAyudaScreen(ir, volver) }
            composable("${Rutas.ARTICULO}/{id}") { ArticuloScreen(it.id(), ir, volver) }
            composable(Rutas.CHATBOT) { ChatbotScreen(ir, volver) }

            // Cuenta
            composable(Rutas.LOGIN) {
                LoginScreen(ir, onSesionIniciada = { nav.irPestana(Rutas.PERFIL) }, onVolver = volver)
            }
            composable(Rutas.CREAR_CUENTA) {
                CrearCuentaScreen(onSesionIniciada = { nav.irPestana(Rutas.PERFIL) }, onVolver = volver)
            }

            // Veterinario y municipio
            composable(Rutas.VET_PANEL) { VetPanelScreen(ir, volver) }
            composable("${Rutas.VET_HISTORIAL}/{id}") { HistorialClinicoScreen(it.id(), onAgregar = { ir(Rutas.VET_NUEVO) }, onVolver = volver) }
            composable(Rutas.VET_NUEVO) { NuevoRegistroClinicoScreen(esVeterinario = true, onVolver = volver) }
            composable(Rutas.MUNICIPAL) { IndicadoresMunicipalesScreen(volver) }

            composable("${Rutas.PROXIMAMENTE}/{titulo}") {
                PantallaProximamente(it.arguments?.getString("titulo").orEmpty(), volver)
            }
        }
    }
}

private data class Pestana(val ruta: String, @StringRes val etiqueta: Int, val icono: ImageVector, val conAviso: Boolean = false)

private val ITEMS_IZQ = listOf(
    Pestana(Rutas.HOME, R.string.nav_inicio, Icons.Rounded.Home),
    Pestana(Rutas.COMUNIDAD, R.string.nav_comunidad, Icons.Rounded.Groups),
)
private val ITEMS_DER = listOf(
    Pestana(Rutas.ALERTAS, R.string.nav_alertas, Icons.Rounded.Notifications, conAviso = true),
    Pestana(Rutas.PERFIL, R.string.nav_perfil, Icons.Rounded.Person),
)

@Composable
fun BarraInferior(rutaActual: String?, onPestana: (String) -> Unit, onRegistrar: () -> Unit) {
    Box(contentAlignment = Alignment.TopCenter) {
        Surface(
            color = Blanco,
            shadowElevation = Dimens.ElevacionBarra,
            modifier = Modifier.padding(top = Dimens.BotonCentralSobresale),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(Dimens.AlturaBarra),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ITEMS_IZQ.forEach { ItemBarra(it, it.ruta == rutaActual, onPestana, Modifier.weight(1f)) }
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(bottom = Dimens.EspacioS),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Text(stringResource(R.string.nav_registrar), style = MaterialTheme.typography.labelSmall, color = TextoSecundario)
                }
                ITEMS_DER.forEach { ItemBarra(it, it.ruta == rutaActual, onPestana, Modifier.weight(1f)) }
            }
        }
        FloatingActionButton(
            onClick = onRegistrar,
            shape = CircleShape,
            containerColor = Turquesa,
            contentColor = Blanco,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = Dimens.ElevacionBarra),
            modifier = Modifier.size(Dimens.BotonCentral),
        ) {
            Icon(Icons.Rounded.Add, stringResource(R.string.nav_registrar), modifier = Modifier.size(Dimens.IconoGrande))
        }
    }
}

@Composable
private fun ItemBarra(item: Pestana, activo: Boolean, onPestana: (String) -> Unit, modifier: Modifier) {
    val color = if (activo) Turquesa else TextoSecundario
    Column(
        modifier
            .fillMaxHeight()
            .selectable(selected = activo, role = Role.Tab, onClick = { onPestana(item.ruta) }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box {
            Icon(item.icono, contentDescription = null, tint = color)
            if (item.conAviso) Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(Dimens.PuntoAviso)
                    .clip(CircleShape)
                    .background(Rojo)
            )
        }
        Text(
            stringResource(item.etiqueta),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Preview
@Composable
private fun BarraInferiorPreview() {
    PetsafeTheme { BarraInferior(Rutas.HOME, {}, {}) }
}
