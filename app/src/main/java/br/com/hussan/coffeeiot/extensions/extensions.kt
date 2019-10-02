package br.com.hussan.coffeeiot.extensions

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Context.color(color: Int): Int {
    return ContextCompat.getColor(this, color)
}
