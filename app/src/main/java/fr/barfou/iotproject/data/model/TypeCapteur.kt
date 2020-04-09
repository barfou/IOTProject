package fr.barfou.iotproject.data.model

import com.google.gson.annotations.SerializedName

data class TypeCapteur(
    @SerializedName("id") val id: Int,
    @SerializedName("nom") val nom: String
)