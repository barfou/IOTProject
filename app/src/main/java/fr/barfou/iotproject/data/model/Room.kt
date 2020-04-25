package fr.barfou.iotproject.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Room(
    @set:Exclude @get:Exclude
    var firebaseId: String,
    var name: String?,
    var presence: Boolean,
    var listLighting: MutableList<Lighting>
) : Serializable {

    fun isAlight(): Boolean = listLighting.filter { it.turnedOn }.isNotEmpty()
}