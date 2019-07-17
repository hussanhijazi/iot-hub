package br.com.hussan.coffeeiot.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Device(val macAddress: String? = null,
                  val userId: String? = null,
                  val type: String? = null) : Parcelable

