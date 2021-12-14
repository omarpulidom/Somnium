package com.example.somniumv4.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.somniumv4.*
import kotlinx.android.synthetic.main.fragment_salud.*
import java.time.LocalDate

class SaludFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_salud, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val day = LocalDate.now().dayOfWeek.name
        val mediciones = PrefConfig.readListFromPref(context, day)

        val bpm = "${getPromedioBPM(mediciones)} bpm"
        bpmText.text = bpm

        val ox = "${getPromedioOx(mediciones)}%"
        oxText.text = ox


        if ((getPromedioBPM(mediciones) < 60 || getPromedioBPM(mediciones) > 100) || (getPromedioOx(
                mediciones
            ) < 90)
        ) {
            resultadosMensaje.setBackgroundResource(R.drawable.rounded_corners_anormal)
            image1.setImageResource(R.drawable.cross)
            resultadosTitle.setText(R.string.anormal_title)
            resultadosDesc.setText(R.string.anormal_desc)
        }

        /*
        clickListeners
        */
        pulsoLayout.setOnClickListener {
            val intent = Intent(activity, SaludGrafsActivity::class.java)
            intent.putExtra("mediciones", mediciones)
            startActivity(intent)
        }
        oxLayout.setOnClickListener {
            val intent = Intent(activity, SaludGrafsActivity::class.java)
            intent.putExtra("mediciones", mediciones)
            startActivity(intent)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun getSpo2(cadena: String): Float {
        val bpm = cadena.substring(cadena.length - 4, cadena.length - 2)
        return bpm.toFloat()
    }

    private fun getBPM(cadena: String): Float {
        val ox = cadena.substring(0, 3)
        return ox.trim().toFloat()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun getPromedioBPM(mediciones: ArrayList<String>): Int {
        var promedioBPM = 0
        for (i in mediciones.indices) {
            promedioBPM += getBPM(mediciones[i]).toInt()
            println(getBPM(mediciones[i]).toInt())
        }
        promedioBPM /= (mediciones.size)
        return promedioBPM
    }

    private fun getPromedioOx(mediciones: ArrayList<String>): Int {
        var promedioOx = 0
        for (i in mediciones.indices) {
            promedioOx += getSpo2(mediciones[i]).toInt()
            println(getSpo2(mediciones[i]).toInt())
        }
        promedioOx /= (mediciones.size)
        return promedioOx
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}