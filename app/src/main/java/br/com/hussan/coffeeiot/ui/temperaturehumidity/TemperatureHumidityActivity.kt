package br.com.hussan.coffeeiot.ui.temperaturehumidity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.hussan.coffeeiot.data.model.Device
import br.com.hussan.coffeeiot.mqtt.MqttClient
import com.example.hussan.coffeeiot.R
import kotlinx.android.synthetic.main.activity_temperature_humidity.txtHumidity
import kotlinx.android.synthetic.main.activity_temperature_humidity.txtTemperature
import org.eclipse.paho.client.mqttv3.MqttMessage

class TemperatureHumidityActivity : AppCompatActivity() {

    val broker: String = "tcp://broker.hivemq.com"

    lateinit var device: Device

    companion object {
        const val DEVICE = "DEVICE"
    }

    val mqttClient by lazy {
        MqttClient(this)
    }

    lateinit var topicTemperature: String
    lateinit var topicHumidity: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_humidity)

        device = intent.getParcelableExtra(DEVICE) as Device

        topicTemperature = "${device.macAddress}/temperature".toLowerCase()
        topicHumidity = "${device.macAddress}/humidity".toLowerCase()

        connectAndSubscribe()
    }

    private fun connectAndSubscribe() {
        mqttClient.connect(broker)
        mqttClient.setCallBack(arrayOf(topicTemperature, topicHumidity), ::setData)
    }

    private fun setData(topic: String, msg: MqttMessage) {
        when (topic) {
            topicTemperature -> {
                txtTemperature.text = " ${String(msg.payload)} ° c"
            }
            else -> {
                txtHumidity.text = " ${String(msg.payload)} %"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }
}

