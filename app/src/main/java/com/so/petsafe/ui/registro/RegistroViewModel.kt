package com.so.petsafe.ui.registro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.domain.SesionFalsa
import com.so.petsafe.domain.Validaciones
import com.so.petsafe.domain.modelo.Animal
import com.so.petsafe.domain.modelo.Especie
import com.so.petsafe.domain.modelo.EstadoAnimal
import com.so.petsafe.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegistroEstado(
    // Paso 1
    val nombre: String = "",
    val especie: String? = null,
    val sexo: String? = null,
    val raza: String = "",
    val color: String = "",
    val edad: String = "",
    val peso: String = "",
    val foto: Boolean = false,
    // Paso 2
    val tienePropietario: String? = null,
    val tieneHogar: String? = null,
    val esterilizado: String? = null,
    val estadoActual: String? = null,
    val observaciones: String = "",
    // Paso 3
    val propietario: String = "",
    val documento: String = "",
    val telefono: String = "",
    val correo: String = "",
    val autoriza: Boolean = false,
) {
    val pesoValido get() = peso.isBlank() || Validaciones.esNumero(peso)
    val paso1Valido get() = nombre.isNotBlank() && especie != null && sexo != null && pesoValido
    val paso2Valido get() = listOf(tienePropietario, tieneHogar, esterilizado, estadoActual).all { it != null }
    val documentoValido get() = documento.count(Char::isDigit) >= 5
    val paso3Valido
        get() = propietario.isNotBlank() && documentoValido && Validaciones.esTelefono(telefono) &&
            Validaciones.esCorreo(correo) && autoriza
}

/** Estado compartido por los 3 pasos + duplicado. Vive en la entrada de navegación del paso 1. */
class RegistroViewModel : ViewModel() {
    private val _estado = MutableStateFlow(RegistroEstado())
    val estado = _estado.asStateFlow()

    fun actualizar(cambio: (RegistroEstado) -> RegistroEstado) = _estado.update(cambio)

    /** Guarda en los datos de demostración y devuelve el id nuevo. */
    fun guardar(especie: Especie, esterilizado: Boolean): String {
        val e = _estado.value
        val id = "nuevo-${System.currentTimeMillis()}"
        DatosDemo.misAnimales.add(
            Animal(
                id = id,
                nombre = e.nombre.trim(),
                especie = especie,
                tipo = e.especie.orEmpty(),
                raza = e.raza.trim(),
                sexo = e.sexo.orEmpty(),
                edad = e.edad.trim(),
                estado = EstadoAnimal.ACTIVO,
                peso = if (e.peso.isBlank()) "" else "${e.peso.trim()} kg",
                esterilizado = esterilizado,
                chip = "",
                responsable = e.propietario.trim().ifBlank { SesionFalsa.NOMBRE },
            )
        )
        return id
    }
}

@Composable
fun ProgresoRegistro(paso: Int, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Text(
            stringResource(R.string.paso_de, paso, 3),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Turquesa,
        )
        Spacer(Modifier.height(Dimens.EspacioS))
        LinearProgressIndicator(
            progress = { paso / 3f },
            color = Turquesa,
            trackColor = Borde,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.AlturaBarraProgreso)
                .clip(RoundedCornerShape(Dimens.RadioS)),
        )
    }
}
