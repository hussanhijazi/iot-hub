package br.com.hussan.coffeeiot.ui.relay

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import br.com.hussan.coffeeiot.extensions.color
import br.com.hussan.coffeeiot.extensions.hide
import br.com.hussan.coffeeiot.extensions.show
import br.com.hussan.coffeeiot.ui.MqttDeviceActivity
import com.example.hussan.coffeeiot.R
import kotlinx.android.synthetic.main.activity_relay.btnCoffee
import kotlinx.android.synthetic.main.activity_relay.imgCoffee
import kotlinx.android.synthetic.main.activity_relay.lytRoot
import kotlinx.android.synthetic.main.activity_relay.progressBar
import org.eclipse.paho.client.mqttv3.MqttMessage

class RelayActivity : MqttDeviceActivity() {

    lateinit var topic: String

    var onOff = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_relay)

        topic = "${device.macAddress}/${device.type}".toLowerCase()

        Log.d("h2", topic)

        connectAndSubscribe(arrayOf(topic))

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

    override fun setData(topic: String, message: MqttMessage) {
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
}
