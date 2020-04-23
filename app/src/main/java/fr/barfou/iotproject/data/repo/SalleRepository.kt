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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.Random.Default.nextBoolean

class SalleRepositoryImpl: SalleRepository {

    private val lesSallesRef = Firebase.database.reference.child("Salles")
    val listSalles = mutableMapOf<String, Salle>()
    val nomSalles = listOf("Salle 2.02", "Salle 0.09", "Salle 0.08")
    val nomEclairages = listOf("Classe", "Tableau")

    override suspend fun retrieveSallesFromFirebase(etablissement_id: Int): Map<String, Salle>? {

        return withContext(Dispatchers.IO) {
            try {
                var success = false
                var complete = false
                // Get Data once when opening the application
                lesSallesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("FirebaseError", error.message)
                        success = false
                        complete = true
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val taskMap = dataSnapshot.value as? HashMap<*, *>
                        var res = true
                        taskMap?.map { entry ->
                            while (res) {
                                val salle = entry.value as HashMap<*, *>
                                val firebaseId = entry.key as String
                                val etablissementId = salle["etablissement_id"] as Int
                                val nom = salle["nom"] as String
                                val presence = salle["presence"] as Boolean
                                listSalles.put(
                                    firebaseId,
                                    Salle(firebaseId, etablissementId, nom, presence, emptyList())
                                )
                                runBlocking {
                                    res = getListEclairageWithId(firebaseId)
                                }
                            }
                        }
                        success = res
                        complete = true
                    }
                })
                while (!complete) {
                    // Task is running
                }
                if (success)
                    return@withContext listSalles
                else
                    return@withContext null
            } catch (ex: Exception) {
                ex.printStackTrace()
                return@withContext null
            }
        }
    }

    private fun getListEclairageWithId(salleFirebaseId: String): Boolean {

        var success = false
        val eclairageRef = lesSallesRef.child(salleFirebaseId).child("listEclairage")
        eclairageRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.d("FirebaseError", error.message)
                success = false
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val listEclairage = mutableListOf<Eclairage>()
                val eclairageMap = dataSnapshot.value as HashMap<*, *>
                eclairageMap.map { entry ->

                    val firebaseId = entry.key as String
                    val eclairage = entry.value as HashMap<*, *>
                    val nom = eclairage["nom"] as String
                    val allume = eclairage["allume"] as Boolean
                    listEclairage.add(Eclairage(firebaseId, salleFirebaseId, nom, allume))
                    if (listSalles.containsKey(salleFirebaseId))
                        listSalles[salleFirebaseId]?.listEclairage = listEclairage
                }
                success = true
            }
        })
        return success
    }

    override suspend fun initFirebase(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                for (salleName in nomSalles) {
                    var idSalle = lesSallesRef.push().key!!
                    val salle1 = Salle(idSalle, 1, salleName, nextBoolean(), emptyList())
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
                    }
                }
                return@withContext true
            } catch (ex: Exception) {
                ex.printStackTrace()
                return@withContext false
            }
        }
    }
}

interface SalleRepository {

    suspend fun retrieveSallesFromFirebase(etablissement_id: Int): Map<String, Salle>?

    suspend fun initFirebase(): Boolean

    companion object {
        val instance: SalleRepository by lazy {
            SalleRepositoryImpl()
        }
    }
}