package fr.barfou.iotproject.data.repo

import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.barfou.iotproject.data.model.Eclairage
import fr.barfou.iotproject.data.model.Salle
import fr.barfou.iotproject.ui.viewmodel.onFinish
import fr.barfou.iotproject.ui.viewmodel.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.Random.Default.nextBoolean

class SalleRepositoryImpl : SalleRepository {

    private val lesSallesRef = Firebase.database.reference.child("Salles")
    val listSalles = mutableListOf<Salle>()
    val nomSalles = listOf("Salle 2.02", "Salle 0.09", "Salle 0.08")
    val nomEclairages = listOf("Classe", "Tableau")

    override suspend fun retrieveDataFromFirebase(onSuccess: onSuccess<List<Salle>?>) {

        if (listSalles.isNotEmpty())
            onSuccess(listSalles)
        else {
            lesSallesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.d("FirebaseError", error.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {

                        val taskMap = dataSnapshot.value as? HashMap<*, *>
                        if (taskMap.isNullOrEmpty())
                            initData().run(onSuccess)
                        else {
                            retrieveData(taskMap)
                            retrieveNestedData {
                                onSuccess(listSalles)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    private fun retrieveData(taskMap: HashMap<*, *>) {
        try {
            taskMap?.map { entry ->
                val salle = entry.value as HashMap<*, *>
                val firebaseId = entry.key as String
                val nom = salle["nom"] as String
                val presence = salle["presence"] as Boolean
                var newSalle = Salle(firebaseId, nom, presence, mutableListOf())
                listSalles.add(newSalle)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun retrieveNestedData(onFinish: onFinish) {
        try {
            listSalles.forEachIndexed { index, salle ->

                val eclairageRef = lesSallesRef.child(salle.firebaseId).child("listEclairage")
                eclairageRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("FirebaseError", error.message)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val listEclairage = mutableListOf<Eclairage>()
                        val eclairageMap = dataSnapshot.value as HashMap<*, *>
                        eclairageMap.map { entry ->
                            val firebaseId = entry.key as String
                            val eclairage = entry.value as HashMap<*, *>
                            val nom = eclairage["nom"] as String
                            val allume = eclairage["allume"] as Boolean
                            listEclairage.add(Eclairage(firebaseId, salle.firebaseId, nom, allume))
                        }
                        listSalles[index]?.listEclairage = listEclairage
                        if (index == listSalles.size - 1)
                            onFinish()
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initData(): List<Salle> {
        try {
            nomSalles.forEachIndexed { index, element ->
                var idSalle = lesSallesRef.push().key!!
                val salle1 = Salle(
                    idSalle, element, nextBoolean(), mutableListOf()
                )
                listSalles.add(salle1)
                lesSallesRef.child(idSalle).setValue(salle1)
                for (eclairageName in nomEclairages) {
                    val salleRef = lesSallesRef.child(idSalle).child("listEclairage")
                    val idEclairage = salleRef.push().key!!
                    val eclairage = Eclairage(
                        idEclairage, idSalle,
                        eclairageName,
                        nextBoolean()
                    )
                    salleRef.child(idEclairage).setValue(eclairage)
                    listSalles[index]?.listEclairage?.add(eclairage)
                }
            }

            return listSalles
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}

interface SalleRepository {

    suspend fun retrieveDataFromFirebase(onSuccess: onSuccess<List<Salle>?>)

    companion object {
        val instance: SalleRepository by lazy {
            SalleRepositoryImpl()
        }
    }
}