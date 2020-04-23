package fr.barfou.iotproject.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Eclairage(
    @set:Exclude @get:Exclude
    var firebaseId: String,
    var salleId: String,
    var nom: String,
    var allume: Boolean
) : Serializable