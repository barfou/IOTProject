package fr.barfou.iotproject.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Salle(
    val etablissement_id: Int,
    val firebaseId: String,
    val nom: String?,
    val presence: Boolean,
    var listEclairage: List<Eclairage>
) : Serializable {

    fun isAlight(): Boolean = listEclairage.filter { it.allume }.isNotEmpty()
}