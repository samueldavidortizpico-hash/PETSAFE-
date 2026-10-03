package com.so.petsafe.ui.componentes

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.so.petsafe.R
import com.so.petsafe.domain.ContactosConfig
import com.so.petsafe.domain.modelo.EstadoAnimal
import com.so.petsafe.domain.modelo.Hito
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// ---------- Colores por significado ----------

fun colorTono(tono: Tono): Color = when (tono) {
    Tono.AMBAR -> Ambar
    Tono.ROJO -> Rojo
    Tono.TURQUESA -> Turquesa
    Tono.VERDE -> Verde
    Tono.NARANJA -> Naranja
    Tono.MORADO -> Morado
}

fun fondoTono(tono: Tono): Color = when (tono) {
    Tono.AMBAR -> AmbarSuave
    Tono.ROJO -> RojoSuave
    Tono.TURQUESA -> TurquesaSuave
    Tono.VERDE -> VerdeSuave
    Tono.NARANJA -> NaranjaSuave
    Tono.MORADO -> MoradoFondo
}

fun textoTono(tono: Tono): Color = when (tono) {
    Tono.AMBAR -> AmbarOscuro
    Tono.ROJO -> RojoOscuro
    Tono.TURQUESA -> TurquesaOscuro
    Tono.VERDE -> VerdeOscuro
    Tono.NARANJA -> NaranjaOscuro
    Tono.MORADO -> Morado
}

@Composable
fun etiquetaEstado(estado: EstadoAnimal): String = stringResource(
    when (estado) {
        EstadoAnimal.ACTIVO -> R.string.estado_activo
        EstadoAnimal.VACUNA_PROXIMA -> R.string.estado_vacuna_proxima
        EstadoAnimal.PERDIDO -> R.string.estado_perdido
    }
)

// ---------- Encabezados ----------

@Composable
fun EncabezadoVolver(
    titulo: String,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    acciones: @Composable RowScope.() -> Unit = {},
) {
    Surface(color = Blanco, shadowElevation = Dimens.ElevacionSuave, modifier = modifier.fillMaxWidth()) {
        Row(
            Modifier
                .statusBarsPadding()
                .heightIn(min = Dimens.AlturaEncabezado)
                .padding(horizontal = Dimens.EspacioXs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.cd_volver), tint = TextoPrincipal)
            }
            Text(
                titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            acciones()
        }
    }
}

@Composable
fun EncabezadoSeccion(titulo: String, subtitulo: String?, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Blanco)
            .statusBarsPadding()
            .padding(Dimens.EspacioL)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        if (subtitulo != null) Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
    }
}

@Composable
fun TituloSeccion(texto: String, modifier: Modifier = Modifier, accion: String? = null, onAccion: () -> Unit = {}) {
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.TactilMin),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextoPrincipal,
            modifier = Modifier.weight(1f),
        )
        if (accion != null) TextButton(onClick = onAccion) {
            Text(accion, color = Turquesa, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ---------- Tarjetas ----------

@Composable
fun TarjetaBase(
    modifier: Modifier = Modifier,
    color: Color = Blanco,
    radio: Dp = Dimens.RadioTarjeta,
    onClick: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit,
) {
    val forma = RoundedCornerShape(radio)
    val colores = CardDefaults.cardColors(containerColor = color)
    val elevacion = CardDefaults.cardElevation(defaultElevation = Dimens.ElevacionSuave)
    if (onClick == null) {
        Card(modifier = modifier, shape = forma, colors = colores, elevation = elevacion, content = contenido)
    } else {
        Card(onClick = onClick, modifier = modifier, shape = forma, colors = colores, elevation = elevacion, content = contenido)
    }
}

@Composable
fun TarjetaAccion(titulo: String, icono: ImageVector, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TarjetaBase(modifier.height(Dimens.AlturaAccion), color = color, radio = Dimens.RadioGrande, onClick = onClick) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(Dimens.EspacioL),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(icono, contentDescription = null, tint = Blanco, modifier = Modifier.size(Dimens.IconoGrande))
            Text(titulo, color = Blanco, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun CajaEmoji(emoji: String, fondo: Color, modifier: Modifier = Modifier, tamano: Dp = Dimens.CajaIcono) {
    Box(
        modifier
            .size(tamano)
            .clip(RoundedCornerShape(Dimens.RadioS))
            .background(fondo),
        contentAlignment = Alignment.Center,
    ) {
        Text(emoji, fontSize = Dimens.EmojiMediano)
    }
}

@Composable
fun FilaNavegable(
    emoji: String,
    titulo: String,
    descripcion: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fondoEmoji: Color = TurquesaSuave,
) {
    TarjetaBase(modifier.fillMaxWidth(), onClick = onClick) {
        Row(Modifier.padding(Dimens.EspacioL), verticalAlignment = Alignment.CenterVertically) {
            CajaEmoji(emoji, fondoEmoji)
            Spacer(Modifier.width(Dimens.EspacioM))
            Column(Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                if (descripcion != null) {
                    Text(descripcion, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                }
            }
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = TextoSecundario)
        }
    }
}

@Composable
fun FilaDato(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.EspacioXs),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(etiqueta, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
        Spacer(Modifier.width(Dimens.EspacioM))
        Text(valor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextoPrincipal, textAlign = TextAlign.End)
    }
}

@Composable
fun Aviso(texto: String, tono: Tono, modifier: Modifier = Modifier, icono: ImageVector = Icons.Rounded.Info) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.RadioS))
            .background(fondoTono(tono))
            .padding(Dimens.EspacioM),
    ) {
        Icon(icono, contentDescription = null, tint = colorTono(tono))
        Spacer(Modifier.width(Dimens.EspacioS))
        Text(texto, style = MaterialTheme.typography.bodyMedium, color = textoTono(tono))
    }
}

// ---------- Pequeños ----------

@Composable
fun ChipFiltro(texto: String, seleccionado: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) },
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.RadioS),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Blanco,
            selectedContainerColor = Turquesa,
            selectedLabelColor = Blanco,
        ),
    )
}

@Composable
fun EstadoBadge(texto: String, tono: Tono, modifier: Modifier = Modifier) {
    Text(
        texto,
        color = textoTono(tono),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.RadioS))
            .background(fondoTono(tono))
            .padding(horizontal = Dimens.EspacioS, vertical = Dimens.EspacioXs),
    )
}

@Composable
fun AvatarAnimal(emoji: String, descripcion: String, modifier: Modifier = Modifier, tamano: Dp = Dimens.Avatar, fondo: Color = TurquesaSuave) {
    Box(
        modifier
            .size(tamano)
            .clip(CircleShape)
            .background(fondo)
            .clearAndSetSemantics { contentDescription = descripcion },
        contentAlignment = Alignment.Center,
    ) {
        Text(emoji, fontSize = if (tamano >= Dimens.AvatarGrande) Dimens.EmojiGrande else Dimens.EmojiMediano)
    }
}

@Composable
fun BuscadorBar(valor: String, onCambio: (String) -> Unit, placeholder: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Rounded.Search, stringResource(R.string.cd_buscar)) },
        singleLine = true,
        shape = RoundedCornerShape(Dimens.RadioTarjeta),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Blanco,
            unfocusedContainerColor = Blanco,
            unfocusedBorderColor = Borde,
            focusedBorderColor = Turquesa,
        ),
    )
}

@Composable
fun LineaTiempo(hitos: List<Hito>, modifier: Modifier = Modifier) {
    Column(modifier) {
        hitos.forEachIndexed { i, hito ->
            Row(Modifier.height(IntrinsicSize.Min)) {
                Column(Modifier.width(Dimens.PuntoLinea), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(Dimens.PuntoLinea)
                            .clip(CircleShape)
                            .background(if (hito.completado) Verde else Borde),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (hito.completado) Icon(
                            Icons.Rounded.Check,
                            stringResource(R.string.cd_completado),
                            tint = Blanco,
                            modifier = Modifier.size(Dimens.IconoPequeno),
                        )
                    }
                    if (i < hitos.lastIndex) Box(
                        Modifier
                            .width(Dimens.AnchoLineaTiempo)
                            .weight(1f)
                            .background(Borde)
                    )
                }
                Spacer(Modifier.width(Dimens.EspacioM))
                Column(Modifier.padding(bottom = Dimens.EspacioL)) {
                    Text(
                        hito.titulo,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (hito.completado) FontWeight.Bold else FontWeight.Normal,
                        color = if (hito.completado) TextoPrincipal else TextoSecundario,
                    )
                    Text(hito.detalle, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                }
            }
        }
    }
}

// ---------- Botones ----------

@Composable
fun BotonPrincipal(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true, color: Color = Turquesa) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.AlturaBoton),
        shape = RoundedCornerShape(Dimens.RadioTarjeta),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Blanco),
    ) {
        Text(texto, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
fun BotonSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: Color = Turquesa, icono: ImageVector? = null) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.AlturaBoton),
        shape = RoundedCornerShape(Dimens.RadioTarjeta),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null)
            Spacer(Modifier.width(Dimens.EspacioS))
        }
        Text(texto, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    }
}

/** Abre el selector de fotos/videos del sistema (no requiere permisos). */
@Composable
fun BotonAdjunto(texto: String, adjunto: Boolean, onAdjuntar: () -> Unit, modifier: Modifier = Modifier, esVideo: Boolean = false, error: String? = null) {
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onAdjuntar()
    }
    Column(modifier.fillMaxWidth()) {
        BotonSecundario(
            texto = if (adjunto) stringResource(R.string.adjunto_listo) else texto,
            onClick = {
                val tipo = if (esVideo) ActivityResultContracts.PickVisualMedia.VideoOnly else ActivityResultContracts.PickVisualMedia.ImageOnly
                lanzador.launch(PickVisualMediaRequest(tipo))
            },
            color = if (error != null) Rojo else Turquesa,
            icono = when {
                adjunto -> Icons.Rounded.CheckCircle
                esVideo -> Icons.Rounded.Videocam
                else -> Icons.Rounded.PhotoCamera
            },
        )
        if (error != null) Text(error, color = Rojo, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = Dimens.EspacioL, top = Dimens.EspacioXs))
    }
}

// ---------- Formularios ----------

/** Campo con error en línea. El error solo aparece después de que el usuario tocó el campo. */
@Composable
fun CampoTextoConError(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    error: String?,
    modifier: Modifier = Modifier,
    obligatorio: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text,
    lineasMin: Int = 1,
    esContrasena: Boolean = false,
) {
    var tocado by rememberSaveable { mutableStateOf(false) }
    var tuvoFoco by remember { mutableStateOf(false) }
    val mostrarError = tocado && error != null
    OutlinedTextField(
        value = valor,
        onValueChange = { tocado = true; onCambio(it) },
        label = { Text(if (obligatorio) stringResource(R.string.etiqueta_obligatoria, etiqueta) else etiqueta) },
        isError = mostrarError,
        supportingText = if (mostrarError) ({ Text(error.orEmpty()) }) else null,
        singleLine = lineasMin == 1,
        minLines = lineasMin,
        keyboardOptions = KeyboardOptions(
            keyboardType = teclado,
            imeAction = if (lineasMin == 1) ImeAction.Next else ImeAction.Default,
            capitalization = if (teclado == KeyboardType.Text) KeyboardCapitalization.Sentences else KeyboardCapitalization.None,
        ),
        visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(Dimens.RadioS),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Blanco,
            unfocusedContainerColor = Blanco,
            focusedBorderColor = Turquesa,
            unfocusedBorderColor = Borde,
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) tuvoFoco = true else if (tuvoFoco) tocado = true
            },
    )
}

fun formatearFecha(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }.format(Date(millis))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoFecha(valor: String, onCambio: (String) -> Unit, etiqueta: String, modifier: Modifier = Modifier) {
    var abierto by rememberSaveable { mutableStateOf(false) }
    var tocado by rememberSaveable { mutableStateOf(false) }
    val mostrarError = tocado && valor.isBlank()
    Box(modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.etiqueta_obligatoria, etiqueta)) },
            trailingIcon = { Icon(Icons.Rounded.CalendarMonth, contentDescription = null) },
            isError = mostrarError,
            supportingText = if (mostrarError) ({ Text(stringResource(R.string.error_obligatorio)) }) else null,
            shape = RoundedCornerShape(Dimens.RadioS),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Blanco, unfocusedContainerColor = Blanco, unfocusedBorderColor = Borde),
            modifier = Modifier.fillMaxWidth(),
        )
        Box(
            Modifier
                .matchParentSize()
                .clickable(onClickLabel = etiqueta, role = Role.Button) { abierto = true }
        )
    }
    if (abierto) {
        val estado = rememberDatePickerState()
        val cerrar = { abierto = false; tocado = true }
        DatePickerDialog(
            onDismissRequest = cerrar,
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let { onCambio(formatearFecha(it)) }
                    cerrar()
                }) { Text(stringResource(R.string.aceptar)) }
            },
            dismissButton = { TextButton(onClick = cerrar) { Text(stringResource(R.string.cancelar)) } },
        ) { DatePicker(state = estado) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectorOpciones(
    etiqueta: String,
    opciones: List<String>,
    seleccionada: String?,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier,
    obligatorio: Boolean = true,
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            if (obligatorio) stringResource(R.string.etiqueta_obligatoria, etiqueta) else etiqueta,
            style = MaterialTheme.typography.labelLarge,
            color = TextoPrincipal,
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioS)) {
            opciones.forEach { opcion -> ChipFiltro(opcion, opcion == seleccionada, { onSeleccion(opcion) }) }
        }
    }
}

@Composable
fun CasillaVerificacion(texto: String, marcado: Boolean, onCambio: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.TactilMin)
            .toggleable(value = marcado, role = Role.Checkbox, onValueChange = onCambio),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = marcado, onCheckedChange = null, colors = CheckboxDefaults.colors(checkedColor = Turquesa))
        Spacer(Modifier.width(Dimens.EspacioS))
        Text(texto, style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
    }
}

// ---------- Pantallas genéricas ----------

@Composable
fun PantallaExito(
    titulo: String,
    mensaje: String,
    modifier: Modifier = Modifier,
    icono: ImageVector = Icons.Rounded.CheckCircle,
    tono: Tono = Tono.VERDE,
    acciones: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(Dimens.EspacioXxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.EspacioL, Alignment.CenterVertically),
    ) {
        Box(
            Modifier
                .size(Dimens.AvatarGrande)
                .clip(CircleShape)
                .background(fondoTono(tono)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icono, contentDescription = null, tint = colorTono(tono), modifier = Modifier.size(Dimens.IconoXl))
        }
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextoPrincipal, textAlign = TextAlign.Center)
        Text(mensaje, style = MaterialTheme.typography.bodyLarge, color = TextoSecundario, textAlign = TextAlign.Center)
        Spacer(Modifier.height(Dimens.EspacioS))
        acciones()
    }
}

@Composable
fun PantallaProximamente(titulo: String, onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(titulo, onVolver) }, containerColor = GrisSuave) { p ->
        PantallaExito(
            titulo = titulo,
            mensaje = stringResource(R.string.proximamente_texto),
            modifier = Modifier.padding(p),
            icono = Icons.Rounded.Construction,
            tono = Tono.TURQUESA,
        ) {
            BotonSecundario(stringResource(R.string.volver), onVolver)
        }
    }
}

// ---------- Contacto externo (sin permisos) ----------

fun abrirWhatsApp(context: Context) =
    abrir(context, Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${ContactosConfig.WHATSAPP}")))

fun marcarEmergencias(context: Context) =
    abrir(context, Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ContactosConfig.EMERGENCIAS}")))

private fun abrir(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, R.string.sin_app, Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
private fun ComponentesPreview() {
    PetsafeTheme {
        Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
            FilaNavegable("🐾", "Rescates", "Casos activos de rescate", {})
            EstadoBadge("Activo", Tono.VERDE)
            Aviso("Aviso informativo", Tono.TURQUESA)
            BotonPrincipal("Continuar", {})
        }
    }
}
