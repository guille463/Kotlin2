package com.example.entrenamiento

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etMinutos = findViewById<EditText>(R.id.etMinutos)
        val etIntensidad = findViewById<EditText>(R.id.etIntensidad)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        val mensajeInicial = tvResultado.text.toString()

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val minutos = etMinutos.text.toString().toIntOrNull()
            val intensidad = etIntensidad.text.toString().toIntOrNull()

            if (nombre.isEmpty()) {
                tvResultado.text = "Nombre obligatorio"
                tvResultado.setTextColor(getColor(R.color.rojo_error))
            } else if (minutos == null || minutos !in 1..300) {
                tvResultado.text = "Minutos no válidos (1-300)"
                tvResultado.setTextColor(getColor(R.color.rojo_error))
            } else if (intensidad == null || intensidad !in 1..10) {
                tvResultado.text = "Intensidad no válida (1-10)"
                tvResultado.setTextColor(getColor(R.color.rojo_error))
            } else {
                val categoria = clasificarIntensidad(intensidad)
                val puntos = calcularPuntos(minutos, intensidad)

                tvResultado.text = "Nombre: $nombre\n" +
                        "Minutos: $minutos\n" +
                        "Intensidad: $intensidad\n" +
                        "Categoría: $categoria\n" +
                        "Puntos: $puntos"
                tvResultado.setTextColor(getColor(R.color.verde_ok))
            }
        }


        btnLimpiar.setOnClickListener {
            etNombre.setText("")
            etMinutos.setText("")
            etIntensidad.setText("")
            tvResultado.text = mensajeInicial
            tvResultado.setTextColor(getColor(R.color.texto_principal))
            etNombre.requestFocus()
        }
    }


    private fun calcularPuntos(minutos: Int, intensidad: Int): Int {
        return minutos * intensidad
    }

    private fun clasificarIntensidad(intensidad: Int): String {
        return when (intensidad) {
            in 1..3 -> "Suave"
            in 4..6 -> "Moderada"
            else -> "Intensa"
        }
    }
}