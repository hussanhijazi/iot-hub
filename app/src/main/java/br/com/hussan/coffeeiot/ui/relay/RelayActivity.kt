package br.com.hussan.coffeeiot.ui.relay

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.hussan.coffeeiot.data.model.Device
import br.com.hussan.coffeeiot.extensions.color
import br.com.hussan.coffeeiot.extensions.hide
import br.com.hussan.coffeeiot.extensions.show
import br.com.hussan.coffeeiot.mqtt.MqttClient
import com.example.hussan.coffeeiot.R
import kotlinx.android.synthetic.main.activity_relay.btnCoffee
import kotlinx.android.synthetic.main.activity_relay.imgCoffee
import kotlinx.android.synthetic.main.activity_relay.lytRoot
import kotlinx.android.synthetic.main.activity_relay.progressBar
import org.eclipse.paho.client.mqttv3.MqttMessage

class RelayActivity : AppCompatActivity() {

    val broker: String = "tcp://broker.hivemq.com"
    lateinit var topic: String
    private val mqttClient: MqttClient by lazy {
        MqttClient(this)
    }
    lateinit var device: Device

    var onOff = false

    companion object {
        const val DEVICE = "DEVICES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_relay)

        device = intent.getParcelableExtra(DEVICE) as Device
        topic = "${device.macAddress}/${device.type}".toLowerCase()

        Log.d("h2", device.toString())
        Log.d("h2", topic)

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

    private fun connectAndSubscribe() {
        mqttClient.connect(broker)
        mqttClient.setCallBack(arrayOf(topic), ::updateButton)
    }

    private fun updateButton(topic: String, message: MqttMessage) {
        imgCoffee.show()
        onOff = if (String(message.payload) == "on") {
            btnCoffee.setColorFilter(color(R.color.colorPrimary))
            imgCoffee.setImageDrawable(ContextCompat.getDrawable(this@RelayActivity, R.drawable.smile))
            lytRoot.setBackgroundColor(color(android.R.color.white))

            true
        } else {
            btnCoffee.setColorFilter(color(android.R.color.darker_gray))
            imgCoffee.setImageDrawable(ContextCompat.getDrawable(this@RelayActivity, R.drawable.upside_down_smile))
            lytRoot.setBackgroundColor(color(android.R.color.black))
            false
        }
        progressBar.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }
}
