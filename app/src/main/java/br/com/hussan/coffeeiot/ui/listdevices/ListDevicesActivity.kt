package br.com.hussan.coffeeiot.ui.listdevices

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.hussan.coffeeiot.AppApplication
import br.com.hussan.coffeeiot.data.DeviceDataSource
import br.com.hussan.coffeeiot.data.DeviceRepository
import br.com.hussan.coffeeiot.data.model.Device
import br.com.hussan.coffeeiot.extensions.hide
import br.com.hussan.coffeeiot.extensions.show
import br.com.hussan.coffeeiot.ui.qrcode.QrCodeActivity
import br.com.hussan.coffeeiot.ui.relay.RelayActivity
import com.example.hussan.coffeeiot.R
import com.google.firebase.auth.FirebaseUser
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_list_devices.pbDevices
import kotlinx.android.synthetic.main.activity_list_devices.rvDevices

class ListDevicesActivity : AppCompatActivity() {

    private val deviceAdapter: DevicesAdapter by lazy {
        DevicesAdapter(::clickDevice)
    }

    private val deviceRepository: DeviceDataSource by lazy {
        DeviceRepository()
    }

    private val user: FirebaseUser? by lazy {
        (application as AppApplication).getUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_devices)

        setupRecyclerView()

        getDevices()
    }

    private fun getDevices() {
        pbDevices.show()
        deviceRepository.getDevices(user?.uid ?: return)
                .addOnSuccessListener { documents ->
                    val docs = documents.toObjects(Device::class.java)
                    deviceAdapter.setItems(docs)
                    pbDevices.hide()
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    pbDevices.hide()
                }
    }

    private fun clickDevice(device: Device) {

        val intent = Intent(this, RelayActivity::class.java).apply {
            putExtra(RelayActivity.DEVICE, device)
        }

        startActivity(intent)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        result?.contents?.let {
            val (macAddress, type) = it.split(";")
            Log.d("h2", "$macAddress -> $type")
            deviceRepository.save(Device(macAddress, user?.uid, type))
            getDevices()
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_config, menu)
        return true
    }

    private fun setupRecyclerView() {

        rvDevices.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ListDevicesActivity)
            adapter = deviceAdapter
        }

    }
}
