package com.wcsm.shopperrotas.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

private val ptBrLocale = Locale("pt", "BR")

fun Double.toBRLString(): String {
    return NumberFormat.getCurrencyInstance(ptBrLocale).format(this)
}

fun String.getMinutesAndSeconds(): String {
    val stringValue = this.padStart(4, '0')
    val hours = stringValue.substring(0, 2)
    val minutes = stringValue.substring(2, 4)
    return "$hours:$minutes"
}

fun String.revertGetMinutesAndSeconds() : String {
    return this.replace(":", "")
}

fun String.toBrazillianDatetime(): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", ptBrLocale)

    return try {
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: "")
    } catch (e: Exception) {
        null
    }
}