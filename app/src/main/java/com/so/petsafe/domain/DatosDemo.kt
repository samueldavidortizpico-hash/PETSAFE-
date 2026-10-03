package com.so.petsafe.domain

import androidx.compose.runtime.mutableStateListOf
import com.so.petsafe.domain.modelo.Alerta
import com.so.petsafe.domain.modelo.Animal
import com.so.petsafe.domain.modelo.AnimalAdopcion
import com.so.petsafe.domain.modelo.Articulo
import com.so.petsafe.domain.modelo.Ayudante
import com.so.petsafe.domain.modelo.CategoriaAlerta
import com.so.petsafe.domain.modelo.Coincidencia
import com.so.petsafe.domain.modelo.Especie
import com.so.petsafe.domain.modelo.EstadoAnimal
import com.so.petsafe.domain.modelo.Hito
import com.so.petsafe.domain.modelo.Paciente
import com.so.petsafe.domain.modelo.RegistroClinico
import com.so.petsafe.domain.modelo.ReporteComunidad
import com.so.petsafe.domain.modelo.SeccionHistorial
import com.so.petsafe.domain.modelo.Tono
import com.so.petsafe.navigation.Rutas

// Datos de demostración en memoria.
// ponytail: se pierden al cerrar la app; Room/Firebase en la fase siguiente.
object DatosDemo {

    val misAnimales = mutableStateListOf(
        Animal("toby", "Toby", Especie.PERRO, "Perro", "Labrador", "Macho", "4 años", EstadoAnimal.ACTIVO, "28 kg", false, "ARG-2023-00847", SesionFalsa.NOMBRE),
        Animal("luna", "Luna", Especie.GATO, "Gata", "Doméstica", "Hembra", "2 años", EstadoAnimal.VACUNA_PROXIMA, "4.2 kg", true, "ARG-2024-01233", SesionFalsa.NOMBRE),
        Animal("max", "Max", Especie.PERRO, "Perro", "Mestizo", "Macho", "5 años", EstadoAnimal.PERDIDO, "22 kg", false, "ARG-2021-00412", SesionFalsa.NOMBRE),
    )

    fun animal(id: String) = misAnimales.firstOrNull { it.id == id }

    fun actualizarEstado(id: String, estado: EstadoAnimal) {
        val i = misAnimales.indexOfFirst { it.id == id }
        if (i >= 0) misAnimales[i] = misAnimales[i].copy(estado = estado)
    }

    val reportes = mutableStateListOf(
        ReporteComunidad("r1", true, "Max", Especie.PERRO, "Mestizo café", "Barrio Centro", "14 ago 2026", "Sin collar. Último avistamiento frente a la plaza principal."),
        ReporteComunidad("r2", false, null, Especie.PERRO, "Labrador dorado", "Av. San Martín", "13 ago 2026", "Sin collar, asustado. Encontrado cerca del semáforo de la avenida."),
        ReporteComunidad("r3", true, "Coco", Especie.AVE, "Cotorra verde", "Barrio Sur", "10 ago 2026", "Verde con pico naranja. Responde al nombre Coco."),
    )

    val adopciones = listOf(
        AnimalAdopcion("simba", "Simba", Especie.PERRO, "Perro", "Mestizo", "Macho", "1 año", true, "Excelente",
            "Juguetón y sociable con niños y otros animales. Rescatado de la calle en abril. Muy cariñoso.",
            listOf("Antirrábica", "Parvovirus", "Moquillo")),
        AnimalAdopcion("mia", "Mia", Especie.GATO, "Gata", "Angora cruzada", "Hembra", "3 años", true, "Muy buena",
            "Tranquila e independiente, ideal para espacios pequeños. Convive bien con otras gatas.",
            listOf("Antirrábica", "Panleucopenia")),
        AnimalAdopcion("bruno", "Bruno", Especie.PERRO, "Perro", "Golden Retriever", "Macho", "2 años", false, "Buena",
            "Energético y cariñoso. Necesita espacio y ejercicio diario. Ideal para familias activas.",
            listOf("Antirrábica", "Parvovirus")),
    )

    fun adopcion(id: String) = adopciones.firstOrNull { it.id == id }

    val alertas = listOf(
        Alerta("a1", "⚠️", "Vacuna próxima a vencer", "La antirrábica de Luna vence en 15 días", "Hoy", CategoriaAlerta.SALUD, Tono.AMBAR, Rutas.conId(Rutas.PERFIL_ANIMAL, "luna")),
        Alerta("a2", "🔍", "Max reportado como perdido", "Última ubicación Barrio Centro hace 2 días", "Hace 2 días", CategoriaAlerta.PERDIDOS, Tono.ROJO, Rutas.conId(Rutas.PERFIL_ANIMAL, "max")),
        Alerta("a3", "📢", "Campaña de vacunación gratuita", "Jornadas de vacunación antirrábica cada sábado de agosto", "Ayer", CategoriaAlerta.COMUNIDAD, Tono.TURQUESA, Rutas.CENTRO_AYUDA),
        Alerta("a4", "✅", "Toby al día", "Todas sus vacunas y controles están vigentes", "Ayer", CategoriaAlerta.SALUD, Tono.VERDE, Rutas.conId(Rutas.PERFIL_ANIMAL, "toby")),
        Alerta("a5", "📋", "Actualización de caso #2026-0847", "El caso pasó a estado En verificación", "Hace 3 días", CategoriaAlerta.CASOS, Tono.NARANJA, Rutas.SEGUIMIENTO),
        Alerta("a6", "🐕", "Nueva información sobre animal perdido", "Un vecino reportó un avistamiento en Barrio Centro", "Hace 4 días", CategoriaAlerta.PERDIDOS, Tono.ROJO, Rutas.PERDIDOS),
        Alerta("a7", "🔔", "Seguimiento pendiente", "Agrega nueva evidencia a tu reporte si la tienes", "Hace 5 días", CategoriaAlerta.CASOS, Tono.MORADO, Rutas.SEGUIMIENTO),
    )

    val articulos = listOf(
        Articulo("abandonado", "🐕", "Encontré un perro abandonado",
            "1. Observa primero a distancia: postura, heridas y si parece asustado o agresivo.\n\n2. Acércate despacio, de lado y sin mirarlo fijamente. Háblale con voz tranquila.\n\n3. Ofrécele agua. Evita darle comida en exceso.\n\n4. Revisa si tiene collar, placa o microchip (cualquier veterinaria puede leerlo).\n\n5. Toma fotos y publica un reporte de animal encontrado en PetSafe con la ubicación exacta.\n\n6. Si está herido, solicita ayuda desde Urgencias."),
        Articulo("asustado", "🤝", "Cómo acercarme a un perro asustado",
            "• Agáchate de lado, sin invadir su espacio.\n\n• No lo persigas: deja que él se acerque.\n\n• Evita movimientos bruscos y el contacto visual directo.\n\n• Señales de estrés: cola entre las patas, orejas hacia atrás, bostezos o lamerse el hocico.\n\n• Si gruñe o muestra los dientes, retírate despacio y pide apoyo."),
        Articulo("primeros-auxilios", "🩹", "Primeros auxilios básicos",
            "• Mantén la calma y protege tu seguridad: un animal con dolor puede morder.\n\n• Si sangra, presiona la herida con una tela limpia.\n\n• No le des medicamentos para personas: muchos son tóxicos para animales.\n\n• Si sospechas fracturas, muévelo lo menos posible, sobre una superficie firme.\n\n• Lleva al animal a un veterinario lo antes posible."),
        Articulo("agresivo", "⚠️", "Qué hacer ante un animal agresivo",
            "• Quédate quieto, de lado, con los brazos junto al cuerpo.\n\n• No corras ni grites: puede activar su instinto de persecución.\n\n• Retrocede lentamente sin darle la espalda.\n\n• Interpón un objeto (mochila, chaqueta) si se acerca.\n\n• Reporta la situación desde Urgencias si representa un riesgo para otras personas."),
        Articulo("mordedura", "🩸", "Qué hacer si un perro me muerde",
            "1. Lava la herida con abundante agua y jabón durante varios minutos.\n\n2. Cubre con una gasa limpia.\n\n3. Acude a un centro de salud: puede requerir valoración y vacuna antirrábica.\n\n4. Si es posible, identifica al animal y a su responsable para conocer su estado de vacunación.\n\n5. No intentes capturar al animal tú mismo."),
        Articulo("emergencia", "🚨", "Cómo actuar ante una emergencia",
            "• Primero tu seguridad: no te expongas en vías con tráfico.\n\n• Señaliza el lugar si hay un animal herido en la vía.\n\n• Usa la opción Urgencias de PetSafe para registrar la ubicación y la situación.\n\n• Si hay personas en riesgo, comunícate con la línea de emergencias.\n\n• Espera la orientación del organismo correspondiente."),
        Articulo("maltrato", "📋", "Cómo reportar posible maltrato",
            "• Registra lo que observas: fecha, hora, lugar y descripción objetiva.\n\n• Si es seguro, toma fotos o video sin confrontar a nadie.\n\n• En PetSafe, el reporte registra una presunción: no afirma que haya habido maltrato.\n\n• El nivel del caso lo asigna un administrador tras revisarlo.\n\n• Puedes seguir el estado del caso y agregar evidencia nueva."),
        Articulo("adopcion", "❤️", "Cómo funciona la adopción",
            "1. Revisa los perfiles de los animales disponibles.\n\n2. Envía una solicitud de adopción con información veraz.\n\n3. Un equipo evalúa la solicitud; puede contactarte para una entrevista o visita.\n\n4. Si la solicitud avanza, se acuerda la entrega responsable.\n\nAdoptar es un compromiso de muchos años: piensa en tiempo, espacio y gastos."),
    )

    fun articulo(id: String) = articulos.firstOrNull { it.id == id }

    val ayudantes = listOf(
        Ayudante("Ana García", "Encontró 2 animales este mes", 340, "🏅 Rescatadora"),
        Ayudante("Luis Pérez", "Reportó 5 casos de maltrato", 210, "🌟 Defensor"),
        Ayudante("Sara Mora", "Facilitó 1 adopción exitosa", 180, "❤️ Padrino"),
    )

    val ultimosRegistros = listOf(
        RegistroClinico("15 Ago 2026", "Vacuna", "Vacuna antirrábica", "Aplicada", true),
        RegistroClinico("02 Abr 2026", "Control", "Control veterinario", "Completado", true),
        RegistroClinico("10 Ene 2026", "Tratamiento", "Tratamiento finalizado", "Finalizado", true),
    )

    val historial = listOf(
        SeccionHistorial("Vacunas", listOf(
            RegistroClinico("15 Ago 2026", "Vacuna", "Antirrábica anual", "Vigente", true),
            RegistroClinico("20 Feb 2026", "Vacuna", "Polivalente (Parvovirus, Moquillo)", "Vigente", true),
        )),
        SeccionHistorial("Enfermedades diagnosticadas", listOf(
            RegistroClinico("10 Dic 2025", "Diagnóstico", "Otitis externa leve", "Resuelta", true),
        )),
        SeccionHistorial("Casos clínicos presuntos", listOf(
            RegistroClinico("05 Jul 2026", "Presunción", "Posible dermatitis alérgica", "En observación", false),
        )),
        SeccionHistorial("Tratamientos anteriores", listOf(
            RegistroClinico("10 Ene 2026", "Tratamiento", "Gotas óticas por 10 días", "Finalizado", true),
        )),
        SeccionHistorial("Tratamientos actuales", emptyList()),
        SeccionHistorial("Medicamentos", listOf(
            RegistroClinico("01 Ago 2026", "Desparasitante", "Antiparasitario oral trimestral", "Activo", true),
        )),
        SeccionHistorial("Alergias", listOf(
            RegistroClinico("05 Jul 2026", "Alergia", "Sensibilidad a pulgas", "Registrada", false),
        )),
        SeccionHistorial("Peso", listOf(
            RegistroClinico("02 Abr 2026", "Control", "28 kg, peso adecuado", "Registrado", true),
        )),
        SeccionHistorial("Esterilización / castración", listOf(
            RegistroClinico("Sin fecha", "Procedimiento", "No realizada", "Pendiente", false),
        )),
        SeccionHistorial("Observaciones", listOf(
            RegistroClinico("02 Abr 2026", "Nota", "Animal tranquilo durante la consulta", "Registrada", true),
        )),
    )

    val coincidencias = listOf(
        Coincidencia("toby", "Toby", "Perro · Labrador · Barrio Centro", 92),
        Coincidencia("bobby", "Bobby", "Perro · Mestizo · Barrio Norte", 74),
    )

    val pacientes = listOf(
        Paciente("toby", "Toby", "Perro · Labrador · ARG-2023-00847", Especie.PERRO),
        Paciente("luna", "Luna", "Gata · Doméstica · ARG-2024-01233", Especie.GATO),
        Paciente("a42", "Animal encontrado #42", "Perro · Sin chip · Av. San Martín", Especie.OTRO),
    )

    val seguimientoCaso = listOf(
        Hito("Reporte recibido", "14 ago 2026 · 10:32", true),
        Hito("En verificación", "15 ago 2026 · 09:00", true),
        Hito("En seguimiento", "16 ago 2026 · Pendiente", false),
        Hito("Nueva evidencia", "Pendiente", false),
        Hito("Revisión", "Pendiente", false),
        Hito("Resolución", "Pendiente", false),
    )

    val avistamientos = listOf(
        Hito("Reporte de pérdida creado", "14 ago 2026 · 08:15", true),
        Hito("Avistamiento cerca de la plaza principal", "14 ago 2026 · 17:40", true),
        Hito("Avistamiento en Calle 10 con Carrera 5", "15 ago 2026 · 07:20", true),
        Hito("Esperando nuevos avistamientos", "Pendiente", false),
    )

    // Indicadores municipales (porcentajes y conteos de demostración).
    const val COBERTURA_VIGENTE = 78
    const val COBERTURA_PROXIMA = 10
    const val COBERTURA_VENCIDA = 12
    val perdidasPorZona = listOf("Centro" to 4, "Norte" to 3, "Sur" to 3, "Este" to 2)
}
