package br.com.hussan.coffeeiot.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.hussan.coffeeiot.data.model.Device
import br.com.hussan.coffeeiot.mqtt.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

abstract class MqttDeviceActivity : AppCompatActivity() {

    private val broker: String = "tcp://broker.hivemq.com"

    lateinit var device: Device

    companion object {
        const val DEVICE = "DEVICE"
    }

    val mqttClient by lazy {
        MqttClient(this)
    }

    abstract fun setData(topic: String, msg: MqttMessage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        device = intent.getParcelableExtra(DEVICE) as Device
    }

    fun connectAndSubscribe(topics: Array<String>) {
        mqttClient.connect(broker)
        mqttClient.setCallBack(topics, ::setData)
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }
}
