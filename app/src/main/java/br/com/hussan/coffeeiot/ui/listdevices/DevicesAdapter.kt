package br.com.hussan.coffeeiot.ui.listdevices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.hussan.coffeeiot.data.model.Device
import com.example.hussan.coffeeiot.R

class DevicesAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder>() {

    var devices = listOf<Device>()

    inner class DeviceViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var txtMacAddres: TextView = view.findViewById(R.id.txtMacAddress)
        var txtType: TextView = view.findViewById(R.id.txtType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DevicesAdapter.DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_devices, parent, false)
        return DeviceViewHolder(view)
    }


    fun setItems(devices: MutableList<Device>) {
        this.devices = devices
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.txtMacAddres.text = device.macAddress
        holder.txtType.text = device.type
    }
}
