package com.so.petsafe.navigation

import android.net.Uri

object Rutas {
    const val HOME = "home"
    const val COMUNIDAD = "comunidad"
    const val ALERTAS = "alertas"
    const val PERFIL = "perfil"

    const val PERDIDOS = "perdidos-encontrados"
    const val REPORTE_PERDIDO = "reporte-perdido"
    const val REPORTE_ENCONTRADO = "reporte-encontrado"
    const val ADOPCIONES = "adopciones"
    const val ADOPCION_DETALLE = "adopcion-detalle"
    const val SOLICITUD_ADOPCION = "solicitud-adopcion"
    const val ADOPCION_ENVIADA = "adopcion-enviada"

    const val REGISTRAR = "registrar-situacion"
    const val PASO1 = "registro-paso1"
    const val PASO2 = "registro-paso2"
    const val PASO3 = "registro-paso3"
    const val DUPLICADO = "duplicado"
    const val REGISTRO_EXITO = "registro-exito"
    const val REGISTRO_ENCONTRADO = "registro-encontrado"

    const val MIS_ANIMALES = "mis-animales"
    const val PERFIL_ANIMAL = "perfil-animal"
    const val HISTORIAL = "historial-clinico"
    const val NFC = "nfc"
    const val TRANSFERIR = "transferir"
    const val MASCOTA_PERDIDA = "mascota-perdida"
    const val NUEVO_REGISTRO = "nuevo-registro"

    const val URGENCIAS = "urgencias"
    const val DENUNCIA = "denuncia"
    const val DENUNCIA_ENVIADA = "denuncia-enviada"
    const val SEGUIMIENTO = "seguimiento-caso"

    const val CENTRO_AYUDA = "centro-ayuda"
    const val ARTICULO = "articulo"
    const val CHATBOT = "chatbot"

    const val LOGIN = "login"
    const val CREAR_CUENTA = "crear-cuenta"

    const val VET_PANEL = "vet-panel"
    const val VET_HISTORIAL = "vet-historial"
    const val VET_NUEVO = "vet-nuevo-registro"
    const val MUNICIPAL = "municipal"
    const val PROXIMAMENTE = "proximamente"

    fun conId(base: String, id: String) = "$base/$id"
    fun proximamente(titulo: String) = "$PROXIMAMENTE/${Uri.encode(titulo)}"
}
