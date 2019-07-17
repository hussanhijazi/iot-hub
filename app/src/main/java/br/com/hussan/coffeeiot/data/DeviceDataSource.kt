package br.com.hussan.coffeeiot.data

import android.util.Log
import br.com.hussan.coffeeiot.data.model.Device
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

interface DeviceDataSource {
    fun save(device: Device)
    fun getDevices(): Task<QuerySnapshot>
}

class DeviceRepository : DeviceDataSource {

    private var db = FirebaseFirestore.getInstance()

    companion object {
        const val DEVICES = "devices"
    }

    override fun save(device: Device) {
        db.collection(DEVICES)
                .add(device)
                .addOnSuccessListener { documentReference ->
                    Log.d("iot", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("iot", "Error adding document", e)
                }
    }

    override fun getDevices(): Task<QuerySnapshot> {
        return db.collection(DEVICES)
                .get()
               

    }
}
