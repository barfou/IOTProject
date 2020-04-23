package fr.barfou.iotproject.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Salle(
    @set:Exclude @get:Exclude
    var firebaseId: String,
    var etablissement_id: Int,
    var nom: String?,
    var presence: Boolean,
    var listEclairage: List<Eclairage>
) : Serializable {

    fun isAlight(): Boolean = listEclairage.filter { it.allume }.isNotEmpty()
}