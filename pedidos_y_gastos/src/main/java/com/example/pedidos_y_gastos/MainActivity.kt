package com.example.pedidos_y_gastos

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

// ======= Constantes =======
const val TARIFA_1KG = 3.50
const val TARIFA_1_5KG = 6.50
const val TARIFA_MAS_5KG = 9.50
const val IMPORTE_ENVIO_GRATIS = 60.0
const val RECARGO_URGENTE = 4.0
const val PESO_MAXIMO = 30.0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO 1: identifica y conecta las vistas por su R.id.
        val etImporte = findViewById<EditText>(R.id.etImporte)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val cbUrgente = findViewById<CheckBox>(R.id.cbUrgente)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        val mensajeInicial = tvResultado.text.toString()

        // TODO 3: programa los eventos de clic y las validaciones.

        // ---- Botón Calcular ----
        btnCalcular.setOnClickListener {
            // LEER
            val importe = etImporte.text.toString().trim().replace(',', '.').toDoubleOrNull()
            val peso = etPeso.text.toString().trim().replace(',', '.').toDoubleOrNull()
            val urgente = cbUrgente.isChecked

            // VALIDAR
            if (importe == null || importe < 0) {
                tvResultado.text = "Importe no válido (debe ser 0 o más)"
                tvResultado.setTextColor(getColor(R.color.rojo_error))
            } else if (peso == null || peso <= 0 || peso > PESO_MAXIMO) {
                tvResultado.text = "Peso no válido (más de 0 y hasta 30 kg)"
                tvResultado.setTextColor(getColor(R.color.rojo_error))
            } else {
                // CALCULAR
                val envio = costeEnvio(peso, importe, urgente)
                val total = importe + envio

                // MOSTRAR
                tvResultado.text = resumen(peso, importe, envio, urgente, total)
                tvResultado.setTextColor(getColor(R.color.verde_ok))
            }
        }

        // ---- Botón Limpiar ----
        btnLimpiar.setOnClickListener {
            etImporte.setText("")
            etPeso.setText("")
            cbUrgente.isChecked = false
            tvResultado.text = mensajeInicial
            tvResultado.setTextColor(getColor(R.color.texto_principal))
            etImporte.requestFocus()
        }

        // TODO 4: comprueba todos los casos de prueba del PDF.
    }

    // TODO 2: crea las funciones pedidas en el enunciado.

    private fun costeEnvio(peso: Double, importe: Double, urgente: Boolean): Double {
        // 1. Tarifa según el peso
        val tarifa = when {
            peso <= 1.0 -> TARIFA_1KG
            peso <= 5.0 -> TARIFA_1_5KG
            else -> TARIFA_MAS_5KG
        }

        // 2. El coste de envío empieza siendo la tarifa
        var costeDeEnvio = tarifa

        // 3. Envío ordinario gratis si el importe es 60 € o más
        if (importe >= IMPORTE_ENVIO_GRATIS) {
            costeDeEnvio = 0.0
        }

        // 4. El recargo urgente se suma siempre (aunque sea gratis)
        if (urgente) {
            costeDeEnvio += RECARGO_URGENTE
        }

        return costeDeEnvio
    }

    private fun resumen(peso: Double, importe: Double, envio: Double, urgente: Boolean, total: Double): String {
        val textoUrgente = if (urgente) "Envío urgente" else "Envío estándar"
        return "RESUMEN\n" +
                "Peso: $peso kg\n" +
                "Importe: ${euros(importe)}\n" +
                "Tipo: $textoUrgente\n" +
                "Gastos de envío: ${euros(envio)}\n" +
                "Total: ${euros(total)}"
    }

    private fun euros(valor: Double): String {
        return String.format(Locale("es", "ES"), "%.2f €", valor)
    }
}