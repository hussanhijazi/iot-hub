package br.com.hussan.coffeeiot.data

import android.content.Context
import android.content.SharedPreferences

interface PrefsDataSource {
    fun saveTopic(topic: String)
    fun getTopic(): String?
    fun saveBroker(topic: String)
    fun getBroker(): String?
}

class PrefsRespository(val context: Context) : PrefsDataSource {
    val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }
    private var broker: String = "tcp://broker.hivemq.com"
    private var topic: String = "gdgfoz/coffeeiot"

    companion object {
        const val TOPIC_PREFS = "TOPIC_PREFS"
        const val BROKER_PREFS = "BROKER_PREFS"
        const val SHARED_PREFS = "SHARED_PREFS"
    }

    override fun saveTopic(topic: String) {
        with(sharedPref.edit()) {
            putString(TOPIC_PREFS, topic)
            commit()
        }
    }

    override fun getTopic(): String? {
        return sharedPref.getString(TOPIC_PREFS, topic)
    }

    override fun saveBroker(broker: String) {
        with(sharedPref.edit()) {
            putString(BROKER_PREFS, broker)
            commit()
        }
    }

    override fun getBroker(): String? {
        return sharedPref.getString(BROKER_PREFS, broker)
    }

}
