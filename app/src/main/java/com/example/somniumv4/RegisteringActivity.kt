package com.example.somniumv4

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registering.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class RegisteringActivity : AppCompatActivity() {

    val mediciones = ArrayList<String>()

    private var btAdapter: BluetoothAdapter? = null
    private var btSocket: BluetoothSocket? = null
    private var activar = false
    private var btConnection: ConnectedThread? = null
    private var hilo = true
    private var initHilo = false
    private var initHilo2 = false
    private var titleHilo = true

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registering)

        val deviceName = intent.getSerializableExtra("deviceName") as String
        val hInicial = intent.getSerializableExtra("hInicial") as String

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Conectando...")
        progressDialog.setMessage("El bluetooth esta conectando")
        progressDialog.show()

        btAdapter = BluetoothAdapter.getDefaultAdapter()
        verificarBluetooth()
        val pairedDeveicesList = btAdapter!!.bondedDevices
        for (pairedDevice in pairedDeveicesList) {
            if (pairedDevice.name == deviceName) {
                address = pairedDevice.address
            }
        }

        /////////////////////////////////////////////////////
        thread(start = true) {
            while (!initHilo) {
                Thread.sleep(1000)
            }
            while (hilo) {
                btConnection!!.write("1")
                Thread.sleep(1000)
            }
        }

        thread(start = true) {
            while (!initHilo2) {
                Thread.sleep(1000)
            }
            while (titleHilo) {
                runOnUiThread {
                    registrandoText.text = "Registrando"
                }
                Thread.sleep(650)
                runOnUiThread {
                    registrandoText.text = "Registrando."
                }
                Thread.sleep(650)
                runOnUiThread {
                    registrandoText.text = "Registrando.."
                }
                Thread.sleep(650)
                runOnUiThread {
                    registrandoText.text = "Registrando..."
                }
                Thread.sleep(650)
            }
        }

        Handler().postDelayed({
            activar = true
            onResume()
            progressDialog.dismiss()
            initHilo2 = true

            Handler().postDelayed({
                initHilo = true
            }, 1500)

        }, 3000)
        //////////////////////////////////////////////////////

        btnStop.setOnClickListener {
            initHilo = true
            hilo = false
            initHilo2 = true
            titleHilo = false

            Handler().postDelayed({
                try {
                    btSocket!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                ////////////////////////////////
                Handler().postDelayed({
                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                    val hFinal: String = simpleDateFormat.format(Date())
                    println("currentDate: $hFinal")

                    val intent = Intent(this, StatsActivity::class.java)
                    intent.putExtra("mediciones", mediciones)
                    intent.putExtra("hInicial", hInicial)
                    intent.putExtra("hFinal", hFinal)
                    startActivity(intent)
                }, 1000)
                ////////////////////////////////

            }, 1000)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID)
    }

    private fun verificarBluetooth() {
        if (!btAdapter!!.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, 1)
        }
    }

    public override fun onResume() {
        super.onResume()
        if (activar) {
            val device: BluetoothDevice = btAdapter!!.getRemoteDevice(address)
            try {
                btSocket = createBluetoothSocket(device)
            } catch (e: IOException) {
                Toast.makeText(baseContext, "La creacción del Socket fallo", Toast.LENGTH_LONG)
                    .show()
            }
            // Establece la conexión con el socket Bluetooth.
            try {
                btSocket!!.connect()
            } catch (e: IOException) {
                try {
                    btSocket!!.close()
                } catch (e2: IOException) {
                }
            }
            btConnection = btSocket?.let { ConnectedThread(it) }
            btConnection!!.start()
        }
    }

    private inner class ConnectedThread(socket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?
        override fun run() {
            val buffer = ByteArray(256)
            var bytes: Int

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream!!.read(buffer)
                    val readMessage = String(buffer, 0, bytes)
                    if (readMessage.length != 1) {
                        println("String recibida: $readMessage")
                        mediciones.add(readMessage)
                    }

                } catch (e: IOException) {
                    break
                }
            }
        }

        //Envio de trama
        fun write(input: String) {
            try {
                mmOutStream!!.write(input.toByteArray())
            } catch (e: IOException) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(baseContext, "La Conexión fallo", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            try {
                tmpIn = socket.inputStream
                tmpOut = socket.outputStream
            } catch (e: IOException) {
            }
            mmInStream = tmpIn
            mmOutStream = tmpOut
        }
    }

    companion object {
        var address: String? = null
        private val BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}