package com.example.aparcamientoportramos

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

// ======= Constantes =======
const val PRECIO_HORA_COCHE = 1.80
const val TOPE_COCHE = 15.0
const val PRECIO_HORA_MOTO = 1.10
const val TOPE_MOTO = 9.0
const val DESCUENTO_NOCTURNO = 0.20
const val MINUTOS_MAXIMOS = 1440

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO 1: identifica y conecta las vistas por su R.id.
        val etMinutos = findViewById<EditText>(R.id.etMinutos)
        val rgVehiculo = findViewById<RadioGroup>(R.id.rgVehiculo)
        val cbNocturno = findViewById<CheckBox>(R.id.cbNocturno)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // TODO 3: programa los eventos de clic y las validaciones.
        btnCalcular.setOnClickListener {
            // LEER
            val minutos = etMinutos.text.toString().trim().toIntOrNull()
            val idVehiculo = rgVehiculo.checkedRadioButtonId
            val nocturno = cbNocturno.isChecked

            // VALIDAR
            if (minutos == null || minutos !in 1..MINUTOS_MAXIMOS) {
                tvResultado.text = "Minutos no válidos (1-1440)"
                tvResultado.setTextColor(getColor(R.color.rojo_error))
            } else if (idVehiculo == -1) {
                tvResultado.text = "Selecciona coche o moto"
                tvResultado.setTextColor(getColor(R.color.rojo_error))
            } else {
                // CALCULAR
                val vehiculo = if (idVehiculo == R.id.rbCoche) "Coche" else "Moto"
                val horas = calcularHoras(minutos)
                val base = importeBase(horas, vehiculo)
                val descuento = descuentoNocturno(base, nocturno)
                val total = base - descuento

                // MOSTRAR
                tvResultado.text = "Vehículo: $vehiculo\n" +
                        "Horas: $horas\n" +
                        "Importe base: ${euros(base)}\n" +
                        "Descuento nocturno: ${euros(descuento)}\n" +
                        "TOTAL: ${euros(total)}"
                tvResultado.setTextColor(getColor(R.color.verde_ok))
            }
        }


    }

    // TODO 2: crea las funciones pedidas en el enunciado.

    private fun calcularHoras(minutos: Int): Int {
        return (minutos + 59) / 60
    }


    private fun importeBase(horas: Int, vehiculo: String): Double {
        val precioHora = if (vehiculo == "Coche") PRECIO_HORA_COCHE else PRECIO_HORA_MOTO
        val tope = if (vehiculo == "Coche") TOPE_COCHE else TOPE_MOTO
        val bruto = horas * precioHora
        return minOf(bruto, tope)
    }


    private fun descuentoNocturno(base: Double, nocturno: Boolean): Double {
        return if (nocturno) base * DESCUENTO_NOCTURNO else 0.0
    }

    private fun euros(valor: Double): String {
        return String.format(Locale("es", "ES"), "%.2f €", valor)
    }
}