package com.example.somniumv4

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity() {

    private var bAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        val hInicial = intent.getSerializableExtra("hInicial") as String

        val pairedDevices: Set<BluetoothDevice> = bAdapter.bondedDevices
        val list = ArrayList<Any>()
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                val devicename = device.name
                list.add(devicename)
            }

            val aAdapter =
                ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, list)
            list_dispositivos.adapter = aAdapter
        }

        list_dispositivos.setOnItemClickListener { _, _, i, _ ->
            run {
                println(list_dispositivos.getItemAtPosition(i).toString())
                val intent = Intent(this, RegisteringActivity::class.java)
                intent.putExtra("deviceName", list_dispositivos.getItemAtPosition(i).toString())
                intent.putExtra("hInicial", hInicial)
                startActivity(intent)
            }
        }

    }
}
