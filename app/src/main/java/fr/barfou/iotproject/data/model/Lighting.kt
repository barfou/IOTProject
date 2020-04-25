package fr.barfou.iotproject.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Lighting(
    @set:Exclude @get:Exclude
    var firebaseId: String,
    var roomId: String,
    var name: String,
    var turnedOn: Boolean
) : Serializable