package com.example.evaluarasignatura

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

var NOTA_MINIMA = 0.0
var NOTA_MAXIMA = 10.0
var PONDERACION_BLOQUE_1 = 0.2
var PONDERACION_BLOQUE_2 = 0.3
var PONDERACION_BLOQUE_3 = 0.5
var NOTA_MEDIA_MIN = 5
var NOTA_MIN_BLOQUE = 4


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO 1: identifica y conecta las vistas por su R.id.
        var bloque1 = findViewById<EditText>(R.id.etBloque1)
        var bloque2 = findViewById<EditText>(R.id.etBloque2)
        var bloque3 = findViewById<EditText>(R.id.etBloque3)
        var btnEvaluar = findViewById<Button>(R.id.btnEvaluar)
        var btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        var tvNota = findViewById<TextView>(R.id.tvNota)
        var tvEstado = findViewById<TextView>(R.id.tvEstado)

        val notaInicial = tvNota.text.toString()
        val estadoInicial = tvEstado.text.toString()

        // TODO 2: crea las funciones pedidas en el enunciado.
        fun calcular_media(notaBloque1: Double, notaBloque2: Double, notaBloque3: Double): Double{
            var media = (notaBloque1 * PONDERACION_BLOQUE_1)+(notaBloque2 * PONDERACION_BLOQUE_2) + (notaBloque3 * PONDERACION_BLOQUE_3)
            return media
        }

        fun comprobar_aprobado(media:Double, notaBloque1: Double, notaBloque2: Double, notaBloque3: Double): Boolean{
            var aprobado = when{
                media < NOTA_MEDIA_MIN -> false
                notaBloque1 < NOTA_MIN_BLOQUE || notaBloque2 < NOTA_MIN_BLOQUE || notaBloque3 < NOTA_MIN_BLOQUE -> false
                else -> true
            }
            return  aprobado
        }

        fun motivo_suspenso(aprobado:Boolean, media:Double, notaBloque1: Double, notaBloque2: Double, notaBloque3: Double):String? {
            var mensaje: String? = null

            if (aprobado) {
                mensaje = null
            } else {
                if (media < NOTA_MEDIA_MIN) {
                    mensaje = "La media no supera el 5 "
                } else if (notaBloque1 < NOTA_MIN_BLOQUE || notaBloque2 < NOTA_MIN_BLOQUE || notaBloque3 < NOTA_MIN_BLOQUE) {
                    mensaje = "Bloque suspenso"
                }
            }
            return  mensaje
        }

        fun calificacion_texto (aprobado: Boolean, media: Double):String{
            var mensaje = when{
                aprobado && media < 7 -> "Aprobado\n"
                aprobado && media < 9 -> "Notable\n"
                aprobado && media >=9 -> "Sobresaliente\n"

                else ->"Suspenso\n"
            }

            return  mensaje

        }

        fun resumen (aprobado:Boolean, media:Double, notaBloque1: Double, notaBloque2: Double, notaBloque3: Double ): String{
            var resumen = "Nota Bloque 1: ${notaBloque1}\nNota Bloque 2: ${notaBloque2}\nNota Bloque 3: ${notaBloque3}\n"
            resumen += calificacion_texto(aprobado, media)
            if(!aprobado){
                resumen +="Motivo: ${motivo_suspenso(aprobado, media, notaBloque1, notaBloque2, notaBloque3)}"
            }

            return resumen

        }




        // TODO 3: programa los eventos de clic y las validaciones.

        btnEvaluar.setOnClickListener {
            //LEER
            var notaBloque1 = bloque1.text.toString().trim().toDoubleOrNull()
            var notaBloque2 = bloque2.text.toString().trim().toDoubleOrNull()
            var notaBloque3 = bloque3.text.toString().trim().toDoubleOrNull()

            //VALIDAR
            if(notaBloque1 == null || notaBloque2 == null || notaBloque3 == null ) {
                tvEstado.text = "No dejes los campos vacios"
                tvEstado.setTextColor(getColor(R.color.rojo_error))
            } else if (notaBloque1 !in NOTA_MINIMA..NOTA_MAXIMA || notaBloque2 !in NOTA_MINIMA..NOTA_MAXIMA || notaBloque3 !in NOTA_MINIMA..NOTA_MAXIMA ){
                tvEstado.text = "Min(0), Max(10)"
                tvEstado.setTextColor(getColor(R.color.rojo_error))

            } else{
                var media = calcular_media(notaBloque1, notaBloque2, notaBloque3)
                var aprobado = comprobar_aprobado(media, notaBloque1, notaBloque2, notaBloque3)
                tvNota.text="Nota final: ${media}"
                tvEstado.text=resumen(aprobado,media,notaBloque1, notaBloque2, notaBloque3 )

                if(aprobado){
                    tvEstado.setTextColor(getColor(R.color.verde_ok))
                } else{
                    tvEstado.setTextColor(getColor(R.color.rojo_error))
                }

            }
        }

        btnLimpiar.setOnClickListener {
            bloque1.setText("")
            bloque2.setText("")
            bloque3.setText("")
            tvNota.text = notaInicial
            tvNota.setTextColor(getColor(R.color.texto_principal))
            tvEstado.text = estadoInicial
            tvEstado.setTextColor(getColor(R.color.texto_principal))
            bloque1.requestFocus()
        }

        // TODO 4: comprueba todos los casos de prueba del PDF.
    }


}


