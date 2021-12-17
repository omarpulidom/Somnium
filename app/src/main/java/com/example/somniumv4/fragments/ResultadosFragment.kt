package com.example.somniumv4.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.somniumv4.PrefConfig
import com.example.somniumv4.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_resultados.*
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

class ResultadosFragment(hInicial: String, hFinal: String) : Fragment() {

    private val horaInicial = hInicial
    private val horaFinal = hFinal

    var ronquidosGroup = ArrayList<BarEntry>()
    var bpmGroup = ArrayList<BarEntry>()
    var oxGroup = ArrayList<BarEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resultados, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val day = LocalDate.now().dayOfWeek.name
        val mediciones = PrefConfig.readListFromPref(context, day)

        val yesterday = LocalDate.now().minusDays(1).dayOfWeek.name
        val medicionesAyer = PrefConfig.readListFromPref(context, yesterday)

        var pDisminucion = 0

        if (getPorcentajeRonquidos(medicionesAyer) != 0) {

                pDisminucion = getPorcentajeRonquidos(medicionesAyer) - getPorcentajeRonquidos(mediciones)

                if (pDisminucion <0)
                {
                    pDisminucion *= -1
                    disminucion_text.text = "AUMENTO"
                }

        }

        porcentajeDisminacion.text = "$pDisminucion%"
        progress_bar.progress = pDisminucion

        ////////////////////////////////////////////////////////////////////////////////////////////
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val dateStart = dateFormat.parse(horaInicial)
        val dateEnd = dateFormat.parse(horaFinal)

        var diferencia = (dateEnd.time - dateStart.time) / 1000
        var dias = 0
        var horas = 0
        var minutos = 0
        if (diferencia > 86400) {
            dias = floor((diferencia / 86400).toDouble()).toInt()
            diferencia -= dias * 86400
        }
        if (diferencia > 3600) {
            horas = floor((diferencia / 3600).toDouble()).toInt()
            diferencia -= horas * 3600
        }
        if (diferencia > 60) {
            minutos = floor((diferencia / 60).toDouble()).toInt()
        }
        println("$horas h $minutos min")
        ////////////////////////////////////////////////////////////////////////////////////////////
        HoraInicial.text = horaInicial.substring(horaInicial.length - 5, horaInicial.length)
        HoraFinal.text = horaFinal.substring(horaInicial.length - 5, horaInicial.length)

        val tDurmiendo = "$horas h $minutos min"
        tiempoDurmiendo.text = tDurmiendo

        val minutosTotal = (horas * 60) + minutos
        val minutosRoncando =
            ((getPorcentajeRonquidos(mediciones).toFloat() / 100f) * minutosTotal).toInt()

        val horasRoncando = (minutosRoncando / 60).toInt()
        val mRoncando = minutosRoncando - (horasRoncando * 60)

        val tRoncando = "$horasRoncando h $mRoncando min"
        tiempoRoncando.text = tRoncando
        ////////////////////////////////////////////////////////////////////////////////////////////

        /*
        grafica ronquidos
        */
        val lineEntry = ArrayList<Entry>()

        for (i in mediciones.indices) {
            lineEntry.add(Entry(i.toFloat(), getSnoring(mediciones.get(i))))
        }

        val lineDataSet = LineDataSet(lineEntry, "Ronquidos")
        lineDataSet.color = resources.getColor(R.color.white)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawFilled(true)

        val data = LineData(lineDataSet)
        ronquidosChart.setTouchEnabled(false)
        ronquidosChart.data = data
        ronquidosChart.description = null
        ronquidosChart.axisRight.isEnabled = false
        ronquidosChart.axisLeft.isEnabled = false
        ronquidosChart.xAxis.isEnabled = false
        ronquidosChart.legend.isEnabled = false
        ronquidosChart.setBackgroundColor(resources.getColor(R.color.chart))
        ronquidosChart.animateXY(1000, 1000)
        /*
        fin grafica ronquidos
         */

        /*
        grafica reporte semanal
         */
        var lunesArray = ArrayList<String>()
        var martesArray = ArrayList<String>()
        var miercolesArray = ArrayList<String>()
        var juevesArray = ArrayList<String>()
        var viernesArray = ArrayList<String>()
        var sabadoArray = ArrayList<String>()
        var domingoArray = ArrayList<String>()

        /*
        recuperamos los arrays
         */
        lunesArray = PrefConfig.readListFromPref(context, "MONDAY")
        martesArray = PrefConfig.readListFromPref(context, "TUESDAY")
        miercolesArray = PrefConfig.readListFromPref(context, "WEDNESDAY")
        juevesArray = PrefConfig.readListFromPref(context, "THURSDAY")
        viernesArray = PrefConfig.readListFromPref(context, "FRIDAY")
        sabadoArray = PrefConfig.readListFromPref(context, "SATURDAY")
        domingoArray = PrefConfig.readListFromPref(context, "SUNDAY")

        /*
         */
        addBarEntries(lunesArray, 0)
        addBarEntries(martesArray, 1)
        addBarEntries(miercolesArray, 2)
        addBarEntries(juevesArray, 3)
        addBarEntries(viernesArray, 4)
        addBarEntries(sabadoArray, 5)
        addBarEntries(domingoArray, 6)

        //data sets
        val ronquidosDataSet = BarDataSet(ronquidosGroup, "Ronquidos")
        ronquidosDataSet.color = resources.getColor(R.color.white)
        val bpmDataSet = BarDataSet(bpmGroup, "BPM")
        bpmDataSet.color = resources.getColor(R.color.graf2)
        val oxDataSet = BarDataSet(oxGroup, "SpO2")
        oxDataSet.color = resources.getColor(R.color.graf1)

        ronquidosDataSet.valueFormatter = PercentFormat()
        bpmDataSet.valueFormatter = DataFormat()
        oxDataSet.valueFormatter = DataFormat()

        ronquidosDataSet.valueTextSize = 6f
        ronquidosDataSet.valueTextColor = resources.getColor(R.color.white)
        bpmDataSet.valueTextSize = 6f
        bpmDataSet.valueTextColor = resources.getColor(R.color.white)
        oxDataSet.valueTextSize = 6f
        oxDataSet.valueTextColor = resources.getColor(R.color.white)

        val xAxisValues = ArrayList<String>()
        xAxisValues.add("X")
        xAxisValues.add("L")
        xAxisValues.add("M")
        xAxisValues.add("M")
        xAxisValues.add("J")
        xAxisValues.add("V")
        xAxisValues.add("S")
        xAxisValues.add("D")

        //////////////////////////////////////////////////////////////
        val xAxis = reporteChart.getXAxis()
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.textSize = 9f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        xAxis.labelCount = 12
        xAxis.mAxisMaximum = 12f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 4f
        xAxis.spaceMax = 4f
        xAxis.axisMaximum = 8f
        xAxis.axisMinimum = 1f
        //////////////////////////////////////////////////////////////
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(ronquidosDataSet)
        dataSets.add(bpmDataSet)
        dataSets.add(oxDataSet)
        val barData = BarData(dataSets)
        val groupSpace = 0.4f
        val barSpace = 0f
        val barWidth = 0.21f

        barData.barWidth = barWidth
        reporteChart.data = barData
        reporteChart.description = null
        reporteChart.axisRight.isEnabled = false
        reporteChart.xAxis.textColor = resources.getColor(R.color.white)
        reporteChart.axisLeft.textColor = resources.getColor(R.color.white)
        reporteChart.legend.textColor = resources.getColor(R.color.white)
        reporteChart.setScaleEnabled(false)
        reporteChart.groupBars(1f, groupSpace, barSpace)
        reporteChart.animateY(1000)
        reporteChart.invalidate()
        /*
        fin grafica reporte semanal
         */
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun getSnoring(cadena: String): Float {
        val startIndex = cadena.length - 1
        val endIndex = cadena.length
        val snore = cadena.substring(startIndex, endIndex)
        return (snore.toFloat() * 100)
    }

    private fun getSpo2(cadena: String): Float {
        val bpm = cadena.substring(cadena.length - 4, cadena.length - 2)
        return bpm.toFloat()
    }

    private fun getBPM(cadena: String): Float {
        val ox = cadena.substring(0, 3)
        return ox.trim().toFloat()
    }

    private fun getPromedioBPM(mediciones: ArrayList<String>): Int {
        var promedioBPM = 0
        for (i in mediciones.indices) {
            promedioBPM += getBPM(mediciones[i]).toInt()
        }
        promedioBPM /= (mediciones.size)
        return promedioBPM
    }

    private fun getPromedioOx(mediciones: ArrayList<String>): Int {
        var promedioOx = 0
        for (i in mediciones.indices) {
            promedioOx += getSpo2(mediciones[i]).toInt()
        }
        promedioOx /= (mediciones.size)
        return promedioOx
    }

    private fun getPorcentajeRonquidos(mediciones: ArrayList<String>): Int {
        var medicionesPositivas = 0
        for (i in mediciones.indices) {
            if (getSnoring(mediciones[i]) == 100f) {
                medicionesPositivas++
            }
        }
        return ((medicionesPositivas.toFloat() / mediciones.size.toFloat()) * 100).toInt()
    }

    private fun addBarEntries(array: ArrayList<String>, nDayOfWeek: Int) {
        if (ronquidosGroup.size != 7) {
            ronquidosGroup.add(
                BarEntry(
                    nDayOfWeek.toFloat(),
                    getPorcentajeRonquidos(array).toFloat()
                )
            )
            bpmGroup.add(BarEntry(nDayOfWeek.toFloat(), getPromedioBPM(array).toFloat()))
            oxGroup.add(BarEntry(nDayOfWeek.toFloat(), getPromedioOx(array).toFloat()))

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}

private class PercentFormat : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value == 0f) {
            ""
        } else {
            "${value.toInt()}%"
        }
    }
}

private class DataFormat : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {

        return if (value == 0f) {
            ""
        } else {
            value.toInt().toString()
        }
    }
}