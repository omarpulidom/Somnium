package com.example.somniumv4

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)


            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val currentDateAndTime: String = simpleDateFormat.format(Date())
            println("currentDate: $currentDateAndTime")

            intent.putExtra("hInicial", currentDateAndTime)

            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()
    }
}