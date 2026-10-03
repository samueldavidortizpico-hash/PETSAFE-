package com.so.petsafe.ui.ayuda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.so.petsafe.R
import com.so.petsafe.domain.RouterChatbot
import com.so.petsafe.domain.modelo.Mensaje
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatbotViewModel : ViewModel() {
    private val _mensajes = MutableStateFlow<List<Mensaje>>(emptyList())
    val mensajes = _mensajes.asStateFlow()

    fun enviar(texto: String) {
        if (texto.isBlank()) return
        val r = RouterChatbot.responder(texto)
        _mensajes.update { it + Mensaje(texto.trim(), deUsuario = true) + Mensaje(r.texto, deUsuario = false, ruta = r.ruta) }
    }
}

@Composable
fun ChatbotScreen(onNavegar: (String) -> Unit, onVolver: () -> Unit, vm: ChatbotViewModel = viewModel()) {
    val mensajes by vm.mensajes.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ChatbotContenido(mensajes, vm::enviar, onNavegar, { abrirWhatsApp(context) }, onVolver)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatbotContenido(
    mensajes: List<Mensaje>,
    onEnviar: (String) -> Unit,
    onNavegar: (String) -> Unit,
    onPersona: () -> Unit,
    onVolver: () -> Unit,
) {
    var texto by rememberSaveable { mutableStateOf("") }
    val lista = rememberLazyListState()
    LaunchedEffect(mensajes.size) { if (mensajes.isNotEmpty()) lista.animateScrollToItem(mensajes.size) }
    val enviar = { onEnviar(texto); texto = "" }

    Scaffold(
        topBar = {
            EncabezadoVolver(stringResource(R.string.chatbot_titulo), onVolver) {
                IconButton(onClick = onPersona) { Icon(Icons.Rounded.SupportAgent, stringResource(R.string.hablar_persona), tint = Turquesa) }
            }
        },
        bottomBar = {
            Surface(color = Blanco, shadowElevation = Dimens.ElevacionBarra) {
                Row(
                    Modifier
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(Dimens.EspacioS),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = texto,
                        onValueChange = { texto = it },
                        placeholder = { Text(stringResource(R.string.escribe_mensaje)) },
                        shape = RoundedCornerShape(Dimens.RadioTarjeta),
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                    )
                    IconButton(onClick = enviar, enabled = texto.isNotBlank()) {
                        Icon(Icons.AutoMirrored.Rounded.Send, stringResource(R.string.enviar), tint = if (texto.isNotBlank()) Turquesa else TextoSecundario)
                    }
                }
            }
        },
        containerColor = GrisSuave,
    ) { p ->
        LazyColumn(
            Modifier.padding(p),
            state = lista,
            contentPadding = PaddingValues(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioS),
        ) {
            item(key = "inicio") {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                    Burbuja(Mensaje(stringResource(R.string.chatbot_saludo), deUsuario = false), onNavegar)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
                        stringArrayResource(R.array.chatbot_rapidos).forEach { r -> ChipFiltro(r, false, { onEnviar(r) }) }
                    }
                    BotonSecundario(stringResource(R.string.hablar_persona), onPersona, icono = Icons.Rounded.SupportAgent)
                }
            }
            itemsIndexed(mensajes, key = { i, _ -> i }) { _, m -> Burbuja(m, onNavegar) }
        }
    }
}

@Composable
private fun Burbuja(m: Mensaje, onNavegar: (String) -> Unit) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = if (m.deUsuario) Alignment.End else Alignment.Start) {
        Text(
            m.texto,
            color = if (m.deUsuario) Blanco else TextoPrincipal,
            modifier = Modifier
                .widthIn(max = Dimens.AnchoBurbuja)
                .clip(RoundedCornerShape(Dimens.RadioTarjeta))
                .background(if (m.deUsuario) Turquesa else Blanco)
                .padding(Dimens.EspacioM),
        )
        if (m.ruta != null) TextButton(onClick = { onNavegar(m.ruta) }) {
            Text(stringResource(R.string.ir_seccion), color = Turquesa)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatbotPreview() {
    PetsafeTheme {
        ChatbotContenido(listOf(Mensaje("Encontré un perro", true), Mensaje("Gracias por ayudar…", false, "x")), {}, {}, {}, {})
    }
}
