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
import kotlinx.coroutines.runBlocking

class SalleRepositoryImpl(
    //private val iotApi: IotApi
) : SalleRepository {

    private val sallesRef = Firebase.database.reference.child("Salles")
    val listSalles = mutableMapOf<String, Salle>()

    override suspend fun retrieveSallesFromFirebase(etablissement_id: Int): Map<String, Salle>? {

        var success = false
        var complete = false
        // Get Data once when opening the application
        sallesRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
                            Salle(etablissementId, firebaseId, nom, presence, emptyList())
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
        return if (success)
            listSalles
        else
            null
    }

    private fun getListEclairageWithId(salleFirebaseId: String): Boolean {

        var success = false
        val eclairageRef = sallesRef.child(salleFirebaseId).child("listEclairage")
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

    /*override suspend fun getAllSalles(etablissement_id: Int): List<Salle> {
        // Test Values
        var listSalle = mutableListOf<Salle>()
        var listEclairage1 = mutableListOf<Eclairage>()
        listEclairage1.add(
            Eclairage(
                1,
                1,
                "Tableau",
                false
            )
        )
        listEclairage1.add(
            Eclairage(
                1,
                1,
                "Classe",
                false
            )
        )
        listSalle.add(
            Salle(
                etablissement_id = 1,
                id = 1,
                nom = "2.02",
                presence = true,
                eclairage = listEclairage1
            )
        )

        var listEclairage2 = mutableListOf<Eclairage>()
        listEclairage2.add(
            Eclairage(
                1,
                1,
                "Tableau",
                true
            )
        )
        listEclairage2.add(
            Eclairage(
                1,
                1,
                "Classe",
                false
            )
        )
        listSalle.add(
            Salle(
                etablissement_id = 1,
                id = 2,
                nom = "0.09",
                presence = false,
                eclairage = listEclairage2
            )
        )
        var listEclairage3 = mutableListOf<Eclairage>()
        listEclairage3.add(
            Eclairage(
                1,
                1,
                "Tableau",
                true
            )
        )
        listEclairage3.add(
            Eclairage(
                1,
                1,
                "Rangée 1",
                true
            )
        )
        listEclairage3.add(
            Eclairage(
                1,
                1,
                "Rangée 2",
                true
            )
        )
        listSalle.add(
            Salle(
                etablissement_id = 1,
                id = 3,
                nom = "0.08",
                presence = true,
                eclairage = listEclairage3
            )
        )
        return listSalle
    }*/
}

interface SalleRepository {

    suspend fun retrieveSallesFromFirebase(etablissement_id: Int): Map<String, Salle>?

    companion object {
        val instance: SalleRepository by lazy {
            SalleRepositoryImpl()
            //SalleRepositoryImpl(HttpClientManager.instance.createApi())
        }
    }
}