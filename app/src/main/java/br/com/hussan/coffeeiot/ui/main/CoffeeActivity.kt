package br.com.hussan.coffeeiot.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.hussan.coffeeiot.data.DeviceDataSource
import br.com.hussan.coffeeiot.data.DeviceRepository
import br.com.hussan.coffeeiot.data.PrefsRespository
import br.com.hussan.coffeeiot.data.model.Device
import br.com.hussan.coffeeiot.extensions.color
import br.com.hussan.coffeeiot.extensions.hide
import br.com.hussan.coffeeiot.extensions.show
import br.com.hussan.coffeeiot.mqtt.MqttClient
import br.com.hussan.coffeeiot.ui.qrcode.QrCodeActivity
import com.example.hussan.coffeeiot.R
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_coffee.btnCoffee
import kotlinx.android.synthetic.main.activity_coffee.imgCoffee
import kotlinx.android.synthetic.main.activity_coffee.lytRoot
import kotlinx.android.synthetic.main.activity_coffee.progressBar
import org.eclipse.paho.client.mqttv3.MqttMessage

class CoffeeActivity : AppCompatActivity() {

    lateinit var broker: String
    lateinit var topic: String
    val prefs: PrefsRespository by lazy {
        PrefsRespository(this)
    }
    val mqttClient: MqttClient by lazy {
        MqttClient(this)
    }

    val deviceRepository: DeviceDataSource by lazy {
        DeviceRepository()
    }

    var onOff = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_coffee)

        getLocalData()

        connectAndSubscribe()

        btnCoffee.setOnClickListener {
            progressBar.show()
            onOff = if (!onOff) {
                mqttClient.publishMessage(topic, "on")
                true
            } else {
                mqttClient.publishMessage(topic, "off")
                false
            }
        }
    }

    private fun getLocalData() {
        broker = prefs?.getBroker() ?: return
        topic = prefs?.getTopic() ?: return
    }

    private fun connectAndSubscribe() {
        mqttClient.connect(broker)
        mqttClient.setCallBack(arrayOf(topic), ::updateButton)
    }

    private fun updateButton(topic: String, message: MqttMessage) {
        imgCoffee.show()
        onOff = if (String(message.payload) == "on") {
            btnCoffee.setColorFilter(color(R.color.colorPrimary))
            imgCoffee.setImageDrawable(ContextCompat.getDrawable(this@CoffeeActivity, R.drawable.smile))
            lytRoot.setBackgroundColor(color(android.R.color.white))

            true
        } else {
            btnCoffee.setColorFilter(color(android.R.color.darker_gray))
            imgCoffee.setImageDrawable(ContextCompat.getDrawable(this@CoffeeActivity, R.drawable.upside_down_smile))
            lytRoot.setBackgroundColor(color(android.R.color.black))
            false
        }
        progressBar.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.qrcode -> {
                IntentIntegrator(this)
                        .setCaptureActivity(QrCodeActivity::class.java)
                        .setPrompt(getString(R.string.msg_read_barcode))
                        .setOrientationLocked(false)
                        .initiateScan()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        result?.contents?.let {
            val (macAddress, type) = it.split(";")
            prefs.saveTopic("$macAddress/coffeeiot")
            Log.d("h2", "$macAddress -> $type")
            deviceRepository.save(Device(macAddress, "100", type))
            getLocalData()
            connectAndSubscribe()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_config, menu)
        return true
    }
}
