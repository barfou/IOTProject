package fr.barfou.iotproject.data.model

import com.google.gson.annotations.SerializedName

data class Capteur(
    @SerializedName("ref") val ref: String,
    @SerializedName("etat") val etat: Boolean
)