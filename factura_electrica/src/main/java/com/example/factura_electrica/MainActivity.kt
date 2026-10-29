package com.example.factura_electrica

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

//===Constantes===
const val MINIMO_CONSUMO = 0.0
const val DIAS_MINIMOS = 1
const val DIAS_MAXIMOS = 31
const val PRECIO_FIJO_DIA = 0.45
const val LIMITE_TRAMO = 100.0
const val PRECIO_TRAMO1 = 0.12
const val PRECIO_TRAMO2 = 0.18
const val DESCUENTO_BONO = 0.25
const val IVA = 0.21
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO 1: identifica y conecta las vistas por su R.id.
        val etKwh = findViewById<EditText>(R.id.etKwh)
        val etDias = findViewById<EditText>(R.id.etDias)
        val cbBono = findViewById<CheckBox>(R.id.cbBonoSocial)
        val btnCalcular = findViewById<Button>(R.id.etBtncalcular)
        val btnLimpiar = findViewById<Button>(R.id.etLimpiar)
        val tvResultado = findViewById<TextView>(R.id.etTvResultado)
        val textoInicial = tvResultado.text

        // TODO 3: programa los eventos de clic y las validaciones.
        btnCalcular.setOnClickListener {
            val kwh = etKwh.text.toString().toDoubleOrNull()
            val dias = etDias.text.toString().toIntOrNull()

            if (kwh == null || kwh < MINIMO_CONSUMO) {
                tvResultado.setTextColor(getColor(R.color.rojo_error))
                tvResultado.text = "Consumo no válido\n(número ≥ 0)"
            } else if (dias == null || dias < DIAS_MINIMOS || dias > DIAS_MAXIMOS) {
                tvResultado.setTextColor(getColor(R.color.rojo_error))
                tvResultado.text = "Días no válidos\n(entero 1-31)"
            } else {
                val fijo = terminoFijo(dias)
                val energia = costeEnergia(kwh)
                val energiaFinal = aplicarBono(energia, cbBono.isChecked)
                val base = fijo + energiaFinal
                val total = totalConIva(base)

                tvResultado.setTextColor(getColor(R.color.texto_principal))
                tvResultado.text = "${formatearEuros(fijo)}\n" +
                        "${formatearEuros(energia)}\n" +
                        "-${formatearEuros(energia - energiaFinal)}\n" +
                        "${formatearEuros(energiaFinal)}\n" +
                        "${formatearEuros(base)}\n" +
                        "${formatearEuros(total - base)}\n" +
                        formatearEuros(total)
            }
        }

        btnLimpiar.setOnClickListener {
            etKwh.setText("")
            etDias.setText("")
            cbBono.isChecked = false
            tvResultado.setTextColor(getColor(R.color.texto_principal))
            tvResultado.text = textoInicial
        }
        // TODO 4: comprueba todos los casos de prueba del PDF.
    }

    // TODO 2: crea las funciones pedidas en el enunciado.
    // 1. Validación
    private fun consumoValido(kilowatios: Double, diasConsumo: Int): Boolean {
        return kilowatios >= MINIMO_CONSUMO && diasConsumo in DIAS_MINIMOS..DIAS_MAXIMOS
    }

    // 2. Término fijo: lo que se paga por día, sin descuento
    private fun terminoFijo(diasConsumo: Int): Double {
        return diasConsumo * PRECIO_FIJO_DIA
    }

    // 3. Coste de energía por tramos (obligatoria)
    private fun costeEnergia(kilowatios: Double): Double {
        if (kilowatios <= LIMITE_TRAMO) {
            return kilowatios * PRECIO_TRAMO1
        }
        val primerTramo = LIMITE_TRAMO * PRECIO_TRAMO1
        val exceso = (kilowatios - LIMITE_TRAMO) * PRECIO_TRAMO2
        return primerTramo + exceso
    }

    // 4. Bono social: solo sobre la energía
    private fun aplicarBono(energia: Double, bonoSocial: Boolean): Double {
        if (bonoSocial) {
            return energia * (1 - DESCUENTO_BONO)
        }
        return energia
    }

    // 5. Total con IVA (obligatoria)
    private fun totalConIva(base: Double): Double {
        return base * (1 + IVA)
    }

    // 6. Formato en euros con dos decimales
    private fun formatearEuros(valor: Double): String {
        return String.format(Locale("es", "ES"), "%.2f €", valor)


    }
}