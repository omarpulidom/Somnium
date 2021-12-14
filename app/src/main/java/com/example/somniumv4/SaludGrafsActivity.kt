package com.example.somniumv4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_salud_grafs.*
import kotlinx.android.synthetic.main.fragment_resultados.*

class SaludGrafsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salud_grafs)

        val mediciones = intent.getSerializableExtra("mediciones") as ArrayList<String>?

        buttonBack.setOnClickListener {
            onBackPressed()
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        val lineEntry = ArrayList<Entry>()
        if (mediciones != null) {
            for (i in mediciones.indices) {
                lineEntry.add(Entry(i.toFloat(), getBPM(mediciones[i])))
            }
        }

        val lineDataSet = LineDataSet(lineEntry, "BPM")
        lineDataSet.color = resources.getColor(R.color.white)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val data = LineData(lineDataSet)

        bpmGraph.setTouchEnabled(false)
        bpmGraph.data = data
        bpmGraph.description = null
        bpmGraph.axisRight.isEnabled = false
        bpmGraph.axisLeft.axisMinimum = 60f
        bpmGraph.xAxis.isEnabled = false
        bpmGraph.animateXY(1000, 1000)
        /*
        fin grafica bpm
         */

        ////////////////////////////////////////////////////////////////////////////////////////////
        val lineEntry2 = ArrayList<Entry>()
        if (mediciones != null) {
            for (i in mediciones.indices) {
                lineEntry2.add(Entry(i.toFloat(), getSpo2(mediciones[i])))
            }
        }

        val lineDataSet2 = LineDataSet(lineEntry2, "SpO2")
        lineDataSet2.color = resources.getColor(R.color.white)
        lineDataSet2.setDrawCircles(false)
        lineDataSet2.setDrawValues(false)
        lineDataSet2.setDrawFilled(true)
        lineDataSet2.mode = LineDataSet.Mode.CUBIC_BEZIER

        val data2 = LineData(lineDataSet2)

        oxGraph.setTouchEnabled(false)
        oxGraph.data = data2
        oxGraph.description = null
        oxGraph.axisRight.isEnabled = false
        oxGraph.axisLeft.axisMinimum = 60f
        oxGraph.axisLeft.axisMaximum = 100f
        oxGraph.xAxis.isEnabled = false
        oxGraph.animateXY(1000, 1000)
        /*
        fin grafica ox
         */

    }

    private fun getBPM(cadena: String): Float {
        val ox = cadena.substring(0, 3)
        return ox.trim().toFloat()
    }

    private fun getSpo2(cadena: String): Float {
        val bpm = cadena.substring(cadena.length - 4, cadena.length - 2)
        return bpm.toFloat()
    }
}