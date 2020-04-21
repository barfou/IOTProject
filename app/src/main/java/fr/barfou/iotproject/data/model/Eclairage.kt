package fr.barfou.iotproject.data.model

import java.io.Serializable

data class Eclairage(
    val firebaseId: String,
    val salleId: String,
    val nom: String,
    val allume: Boolean
) : Serializable