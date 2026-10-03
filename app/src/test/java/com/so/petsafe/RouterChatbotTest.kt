package com.so.petsafe

import com.so.petsafe.domain.RouterChatbot
import com.so.petsafe.navigation.Rutas
import org.junit.Assert.assertEquals
import org.junit.Test

class RouterChatbotTest {
    @Test
    fun accesosRapidosLlevanALaPantallaCorrecta() {
        assertEquals(Rutas.REPORTE_ENCONTRADO, RouterChatbot.responder("Encontré un perro").ruta)
        assertEquals(Rutas.REPORTE_PERDIDO, RouterChatbot.responder("Mi mascota está perdida").ruta)
        assertEquals(Rutas.URGENCIAS, RouterChatbot.responder("Necesito ayuda urgente").ruta)
        assertEquals(Rutas.DENUNCIA, RouterChatbot.responder("Quiero denunciar").ruta)
        assertEquals(Rutas.ADOPCIONES, RouterChatbot.responder("Quiero adoptar").ruta)
        assertEquals(Rutas.CENTRO_AYUDA, RouterChatbot.responder("hola").ruta)
    }
}
