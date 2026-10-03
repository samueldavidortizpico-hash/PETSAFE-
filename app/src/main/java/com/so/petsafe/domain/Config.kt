package com.so.petsafe.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ContactosConfig {
    // CAMBIAR: número real de WhatsApp de atención, formato internacional sin "+".
    const val WHATSAPP = "573000000000"
    const val EMERGENCIAS = "123"
}

/** Sesión simulada hasta conectar autenticación real (fase siguiente). */
object SesionFalsa {
    var haySesion by mutableStateOf(false)
    const val NOMBRE = "Carlos Mendez"
    const val CORREO = "carlos.mendez@correo.com"
    const val TELEFONO = "300 123 4567"
}

object Validaciones {
    private val correo = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    fun esCorreo(texto: String) = correo.matches(texto.trim())
    fun esTelefono(texto: String) = texto.count(Char::isDigit) >= 7 && texto.all { it.isDigit() || it in " +-()" }
    fun esNumero(texto: String) = texto.replace(',', '.').toDoubleOrNull() != null
}
