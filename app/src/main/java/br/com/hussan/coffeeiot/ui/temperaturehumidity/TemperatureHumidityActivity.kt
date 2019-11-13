package br.com.hussan.coffeeiot.ui.temperaturehumidity

import android.os.Bundle
import br.com.hussan.coffeeiot.ui.MqttDeviceActivity
import com.example.hussan.coffeeiot.R
import kotlinx.android.synthetic.main.activity_temperature_humidity.txtHumidity
import kotlinx.android.synthetic.main.activity_temperature_humidity.txtTemperature
import org.eclipse.paho.client.mqttv3.MqttMessage

class TemperatureHumidityActivity : MqttDeviceActivity() {

    lateinit var topicTemperature: String
    lateinit var topicHumidity: String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_humidity)

        topicTemperature = "${device.macAddress}/temperature".toLowerCase()
        topicHumidity = "${device.macAddress}/humidity".toLowerCase()

        connectAndSubscribe(arrayOf(topicTemperature, topicHumidity))
    }

    override fun setData(topic: String, msg: MqttMessage) {
        when (topic) {
            topicTemperature -> {
                txtTemperature.text = " ${String(msg.payload)} ° c"
            }
            else -> {
                txtHumidity.text = " ${String(msg.payload)} %"
            }
        }
    }

}

