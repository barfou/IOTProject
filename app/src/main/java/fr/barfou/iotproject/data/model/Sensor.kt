package fr.barfou.iotproject.data.model

import com.google.gson.annotations.SerializedName

data class Sensor(
    val ref: String,
    val state: Boolean
)