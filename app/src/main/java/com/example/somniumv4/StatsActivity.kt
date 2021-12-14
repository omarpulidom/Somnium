package com.example.somniumv4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.somniumv4.fragments.ResultadosFragment
import com.example.somniumv4.fragments.SaludFragment
import kotlinx.android.synthetic.main.activity_stats.*
import java.time.LocalDate
import kotlin.collections.ArrayList

class StatsActivity : AppCompatActivity() {
    private val arrayVacio = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        arrayVacio.add("00 00 0")

        val mediciones = intent.getSerializableExtra("mediciones") as ArrayList<String>

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Parses the date

        val day = LocalDate.now().dayOfWeek.name
        println(day)

        if (day == "MONDAY") {
            PrefConfig.writeListInPref(this, "MONDAY", arrayVacio)
            PrefConfig.writeListInPref(this, "TUESDAY", arrayVacio)
            PrefConfig.writeListInPref(this, "WEDNESDAY", arrayVacio)
            PrefConfig.writeListInPref(this, "THURSDAY", arrayVacio)
            PrefConfig.writeListInPref(this, "FRIDAY", arrayVacio)
            PrefConfig.writeListInPref(this, "SATURDAY", arrayVacio)
            PrefConfig.writeListInPref(this, "SUNDAY", arrayVacio)
        }

        PrefConfig.writeListInPref(this, day, mediciones)
        ////////////////////////////////////////////////////////////////////////////////////////////


        val hInicial = intent.getStringExtra("hInicial").toString()
        val hFinal = intent.getStringExtra("hFinal").toString()

        val saludFragment = SaludFragment()
        val resultadosFragment = ResultadosFragment(hInicial, hFinal)

        makeCurrentFragment(resultadosFragment)

        bottom_navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_resultados -> makeCurrentFragment(resultadosFragment)
                R.id.navigation_salud -> makeCurrentFragment(saludFragment)
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_wrapper, fragment)
        transaction.commit()
    }
}