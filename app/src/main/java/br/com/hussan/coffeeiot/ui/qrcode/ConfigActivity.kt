package br.com.hussan.coffeeiot.ui.qrcode

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.hussan.coffeeiot.data.PrefsRespository
import com.example.hussan.coffeeiot.R
import kotlinx.android.synthetic.main.activity_config.btnSave
import kotlinx.android.synthetic.main.activity_config.edtBroker
import kotlinx.android.synthetic.main.activity_config.edtTopic


class ConfigActivity : AppCompatActivity() {

    val prefs: PrefsRespository by lazy {
        PrefsRespository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_config)
        btnSave.setOnClickListener {
            if (edtBroker.text?.isEmpty() == true
                    || edtTopic.text?.isEmpty() == true) {
                Toast.makeText(this, R.string.type_all_fields, Toast.LENGTH_LONG).show()
            } else {

                prefs.saveTopic(edtTopic.text.toString())
                prefs.saveBroker("tcp://${edtBroker.text.toString().trim()}")

                val intent = intent
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
