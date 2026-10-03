package com.so.petsafe.domain

import com.so.petsafe.navigation.Rutas

/** Atención inicial por reglas locales (sin IA ni red). */
object RouterChatbot {

    data class Respuesta(val texto: String, val ruta: String?)

    private fun String.contieneAlguna(vararg claves: String) = claves.any { it in this }

    fun responder(entrada: String): Respuesta {
        val t = entrada.lowercase()
        return when {
            t.contieneAlguna("urgente", "herido", "accidente", "sangr", "emergencia", "atropell") -> Respuesta(
                "Si el animal está en peligro inmediato, solicita ayuda desde Urgencias. Mantén una distancia segura y no lo muevas si está muy herido.",
                Rutas.URGENCIAS)
            t.contieneAlguna("maltrat", "denunc", "golpe", "abus") -> Respuesta(
                "Puedes reportar una posible situación de maltrato. El caso será revisado; PetSafe registra una presunción, no una acusación.",
                Rutas.DENUNCIA)
            t.contieneAlguna("mord") -> Respuesta(
                "Lava la herida con agua y jabón durante varios minutos y acude a un centro de salud. Aquí tienes la guía completa.",
                Rutas.conId(Rutas.ARTICULO, "mordedura"))
            t.contieneAlguna("perdid", "extravi", "escap") -> Respuesta(
                "Lamento lo de tu mascota. Publica un reporte de animal perdido con foto y última ubicación conocida para que la comunidad te ayude.",
                Rutas.REPORTE_PERDIDO)
            t.contieneAlguna("encontr", "abandon", "calle") -> Respuesta(
                "Gracias por ayudar. Acércate con calma, ofrécele agua y revisa si tiene placa. Luego publica un reporte de animal encontrado.",
                Rutas.REPORTE_ENCONTRADO)
            t.contieneAlguna("adopt") -> Respuesta(
                "¡Qué buena decisión! Conoce a los animales que esperan un hogar y envía tu solicitud para evaluación.",
                Rutas.ADOPCIONES)
            else -> Respuesta(
                "No estoy seguro de haber entendido. Puedes revisar el Centro de ayuda o hablar con una persona.",
                Rutas.CENTRO_AYUDA)
        }
    }
}
