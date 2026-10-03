package com.so.petsafe.domain.modelo

/** Significado del color: verde positivo, ámbar atención, rojo urgente, turquesa informativo. */
enum class Tono { AMBAR, ROJO, TURQUESA, VERDE, NARANJA, MORADO }

enum class Especie(val emoji: String) { PERRO("🐕"), GATO("🐈"), AVE("🦜"), OTRO("🐾") }

enum class EstadoAnimal(val tono: Tono) {
    ACTIVO(Tono.VERDE), VACUNA_PROXIMA(Tono.AMBAR), PERDIDO(Tono.ROJO)
}

data class Animal(
    val id: String,
    val nombre: String,
    val especie: Especie,
    val tipo: String,
    val raza: String,
    val sexo: String,
    val edad: String,
    val estado: EstadoAnimal,
    val peso: String,
    val esterilizado: Boolean,
    val chip: String,
    val responsable: String,
)

data class ReporteComunidad(
    val id: String,
    val perdido: Boolean,
    val nombre: String?,
    val especie: Especie,
    val raza: String,
    val zona: String,
    val fecha: String,
    val descripcion: String,
)

data class AnimalAdopcion(
    val id: String,
    val nombre: String,
    val especie: Especie,
    val tipo: String,
    val raza: String,
    val sexo: String,
    val edad: String,
    val esterilizado: Boolean,
    val salud: String,
    val descripcion: String,
    val vacunas: List<String>,
)

enum class CategoriaAlerta { SALUD, PERDIDOS, CASOS, COMUNIDAD }

data class Alerta(
    val id: String,
    val emoji: String,
    val titulo: String,
    val descripcion: String,
    val tiempo: String,
    val categoria: CategoriaAlerta,
    val tono: Tono,
    val destino: String,
)

data class Articulo(val id: String, val emoji: String, val titulo: String, val cuerpo: String)

data class Ayudante(val nombre: String, val logro: String, val puntos: Int, val insignia: String)

data class RegistroClinico(
    val fecha: String,
    val tipo: String,
    val descripcion: String,
    val estado: String,
    val verificado: Boolean,
)

data class SeccionHistorial(val titulo: String, val registros: List<RegistroClinico>)

data class Coincidencia(val id: String, val nombre: String, val descripcion: String, val porcentaje: Int)

data class Paciente(val id: String, val nombre: String, val detalle: String, val especie: Especie)

data class Hito(val titulo: String, val detalle: String, val completado: Boolean)

data class Mensaje(val texto: String, val deUsuario: Boolean, val ruta: String? = null)
