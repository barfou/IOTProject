package fr.barfou.iotproject.data.repo

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.barfou.iotproject.data.model.Lighting
import fr.barfou.iotproject.data.model.Room
import fr.barfou.iotproject.ui.viewmodel.onFinish
import fr.barfou.iotproject.ui.viewmodel.onSuccess
import kotlin.random.Random.Default.nextBoolean

class RoomRepositoryImpl : RoomRepository {

    private val roomsRef = Firebase.database.reference.child("Rooms")
    val roomsList = mutableListOf<Room>()
    private val roomsNames = listOf("Salle 2.02", "Salle 0.09", "Salle 0.08")
    private val lightingNames = listOf("Classe", "Tableau")

    override suspend fun retrieveDataFromFirebase(onSuccess: onSuccess<List<Room>?>) {

        if (roomsList.isNotEmpty())
            onSuccess(roomsList)
        else {
            roomsRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
                                onSuccess(roomsList)
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
            taskMap.map { entry ->
                val room = entry.value as HashMap<*, *>
                val firebaseId = entry.key as String
                val nom = room["name"] as String
                val presence = room["presence"] as Boolean
                val newRoom = Room(firebaseId, nom, presence, mutableListOf())
                roomsList.add(newRoom)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun retrieveNestedData(onFinish: onFinish) {
        try {
            roomsList.forEachIndexed { index, room ->

                val lightingRef = roomsRef.child(room.firebaseId).child("listLighting")
                lightingRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("FirebaseError", error.message)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val listLighting = mutableListOf<Lighting>()
                        val lightingMap = dataSnapshot.value as HashMap<*, *>
                        lightingMap.map { entry ->
                            val firebaseId = entry.key as String
                            val lighting = entry.value as HashMap<*, *>
                            val name = lighting["name"] as String
                            val turnedOn = lighting["turnedOn"] as Boolean
                            listLighting.add(Lighting(firebaseId, room.firebaseId, name, turnedOn))
                        }
                        roomsList[index].listLighting = listLighting
                        if (index == roomsList.size - 1)
                            onFinish()
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initData(): List<Room> {
        try {
            roomsNames.forEachIndexed { index, element ->
                val roomId = roomsRef.push().key!!
                val newRoom = Room(
                    roomId, element, nextBoolean(), mutableListOf()
                )
                roomsList.add(newRoom)
                roomsRef.child(roomId).setValue(newRoom)
                for (name in lightingNames) {
                    val lightingRef = roomsRef.child(roomId).child("listLighting")
                    val lightId = lightingRef.push().key!!
                    val lighting = Lighting(
                        lightId, roomId,
                        name,
                        nextBoolean()
                    )
                    lightingRef.child(lightId).setValue(lighting)
                    roomsList[index].listLighting.add(lighting)
                }
            }
            return roomsList
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}

interface RoomRepository {

    suspend fun retrieveDataFromFirebase(onSuccess: onSuccess<List<Room>?>)

    companion object {
        val instance: RoomRepository by lazy {
            RoomRepositoryImpl()
        }
    }
}