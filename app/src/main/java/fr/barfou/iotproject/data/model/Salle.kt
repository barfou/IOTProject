package fr.barfou.iotproject.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Salle(
    @SerializedName("etablissement_id") val etablissement_id: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("nom") val nom: String?,
    @SerializedName("presence") val presence: Boolean,
    @SerializedName("eclairage") val eclairage: List<Eclairage>
) : Serializable {

    fun isAlight(): Boolean = eclairage.filter { it.allume }.isNotEmpty()
}