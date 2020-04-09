package fr.barfou.iotproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Eclairage(
    @SerializedName("salle_id") val salle_id: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("nom") val nom: String,
    @SerializedName("allume") val allume: Boolean
) : Serializable