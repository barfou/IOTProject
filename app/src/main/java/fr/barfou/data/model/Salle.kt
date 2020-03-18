package fr.barfou.data.model

import com.google.gson.annotations.SerializedName

data class Salle(
    @SerializedName("etablissement_id") val etablissement_id: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("nom") val nom: String,
    @SerializedName("presence") val presence: Boolean,
    @SerializedName("eclairage") val eclairage: List<Eclairage>
) {
    fun isAlight(): Boolean = eclairage.filter { it.allume }.isNotEmpty()
}