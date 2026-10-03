package com.so.petsafe.ui.municipal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.so.petsafe.R
import com.so.petsafe.domain.DatosDemo
import com.so.petsafe.ui.componentes.*
import com.so.petsafe.ui.theme.*

private const val PORCENTAJE_TOTAL = 100f

@Composable
fun IndicadoresMunicipalesScreen(onVolver: () -> Unit) {
    Scaffold(topBar = { EncabezadoVolver(stringResource(R.string.indicadores_municipales), onVolver) }, containerColor = GrisSuave) { p ->
        Column(
            Modifier
                .padding(p)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.EspacioL),
            verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM),
        ) {
            Text(stringResource(R.string.muni_periodo), style = MaterialTheme.typography.labelLarge, color = TextoSecundario)
            TarjetaBase(Modifier.fillMaxWidth(), color = Turquesa) {
                Column(Modifier.padding(Dimens.EspacioL)) {
                    Text(stringResource(R.string.muni_total), color = Blanco, fontSize = Dimens.NumeroXl, fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.muni_registrados), color = BlancoSuave)
                }
            }
            TituloSeccion(stringResource(R.string.muni_cobertura))
            TarjetaBase(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
                    BarraDato(stringResource(R.string.muni_vigente), DatosDemo.COBERTURA_VIGENTE, Verde)
                    BarraDato(stringResource(R.string.muni_vence_30), DatosDemo.COBERTURA_PROXIMA, Ambar)
                    BarraDato(stringResource(R.string.muni_vencida), DatosDemo.COBERTURA_VENCIDA, Rojo)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
                Indicador(stringResource(R.string.home_num_perdidos), stringResource(R.string.home_perdidos_activos), Rojo, Modifier.weight(1f))
                Indicador(stringResource(R.string.muni_dias), stringResource(R.string.muni_recuperacion), Turquesa, Modifier.weight(1f))
            }
            TituloSeccion(stringResource(R.string.muni_por_zona))
            TarjetaBase(Modifier.fillMaxWidth()) {
                val maximo = DatosDemo.perdidasPorZona.maxOf { it.second }
                Column(Modifier.padding(Dimens.EspacioL), verticalArrangement = Arrangement.spacedBy(Dimens.EspacioM)) {
                    DatosDemo.perdidasPorZona.forEach { (zona, n) ->
                        BarraDato(zona, n, Naranja, fraccion = n / maximo.toFloat(), sufijoPorcentaje = false)
                    }
                }
            }
        }
    }
}

@Composable
private fun BarraDato(etiqueta: String, valor: Int, color: Color, fraccion: Float = valor / PORCENTAJE_TOTAL, sufijoPorcentaje: Boolean = true) {
    Column {
        Row {
            Text(etiqueta, color = TextoPrincipal, modifier = Modifier.weight(1f))
            Text(
                if (sufijoPorcentaje) stringResource(R.string.porcentaje, valor) else valor.toString(),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal,
            )
        }
        Spacer(Modifier.height(Dimens.EspacioXs))
        LinearProgressIndicator(
            progress = { fraccion },
            color = color,
            trackColor = Borde,
            drawStopIndicator = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.AlturaBarraDato)
                .clip(RoundedCornerShape(Dimens.RadioS)),
        )
    }
}

@Composable
private fun Indicador(numero: String, etiqueta: String, color: Color, modifier: Modifier) {
    TarjetaBase(modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.EspacioL),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(numero, fontSize = Dimens.NumeroXl, fontWeight = FontWeight.Bold, color = color)
            Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = TextoSecundario)
        }
    }
}

@Preview(showBackground = true, heightDp = 1100)
@Composable
private fun MunicipalPreview() {
    PetsafeTheme { IndicadoresMunicipalesScreen {} }
}
