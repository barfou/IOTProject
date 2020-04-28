package fr.barfou.iotproject.data.repo

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.barfou.iotproject.data.model.Lighting
import fr.barfou.iotproject.data.model.Room
import fr.barfou.iotproject.ui.viewmodel.onFinish
import fr.barfou.iotproject.ui.viewmodel.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random.Default.nextBoolean

class RoomRepositoryImpl : RoomRepository {

    private val roomsRef = Firebase.database.reference.child("Rooms")
    val roomsList = mutableListOf<Room>()
    private val roomsId = listOf("Rm202", "Rm009", "Rm008")
    private val lightingId = listOf("Classe", "Tableau")

    override suspend fun turnOffTheLight(roomId: String): Room? {
        return withContext(Dispatchers.IO) {
            try {
                val position = roomId.indexOf(roomId)
                roomsRef.child(roomId).child("listLighting").child(lightingId[0]).child("turnedOn").setValue(false)
                roomsRef.child(roomId).child("listLighting").child(lightingId[1]).child("turnedOn").setValue(false)
                roomsList[position].switchOffTheLight()
                return@withContext roomsList[position]
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

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
                            retrieveData(taskMap)?.run(onSuccess)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    private fun retrieveData(taskMap: HashMap<*, *>): List<Room>? {
        try {
            var temp = mutableListOf<Room>()
            taskMap.map { entry ->
                val room = entry.value as HashMap<*, *>
                val firebaseId = entry.key as String
                val nom = room["name"] as String
                val presence = room["presence"] as Boolean

                // Retrieve nested data
                val listLightingRef = room["listLighting"] as HashMap<*,*>
                val listLighting = mutableListOf<Lighting>()
                listLightingRef.map { lightingEntry ->
                    val lightingFirebaseId = lightingEntry.key as String
                    val lighting = lightingEntry.value as HashMap<*, *>
                    val lightingName = lighting["name"] as String
                    val turnedOn = lighting["turnedOn"] as Boolean
                    listLighting.add(Lighting(lightingFirebaseId, firebaseId, lightingName, turnedOn))
                }

                // Build the list
                val newRoom = Room(firebaseId, nom, presence, listLighting)
                if (firebaseId == roomsId[0])
                    roomsList.add(newRoom)
                else
                    temp.add(newRoom)
            }
            roomsList.addAll(temp)
            return roomsList
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun initData(): List<Room> {
        try {
            roomsId.forEachIndexed { index, roomId ->
                var roomName = roomId.replace("Rm", "Salle ")
                roomName = roomName.substring(0, 7) + "." + roomName.substring(7, roomName.length);
                val newRoom = Room(
                    roomId, roomName, nextBoolean(), mutableListOf()
                )
                roomsList.add(newRoom)
                roomsRef.child(roomId).setValue(newRoom)
                for (lightId in lightingId) {
                    val lightingRef = roomsRef.child(roomId).child("listLighting")
                    //val lightId = lightingRef.push().key!!
                    val lighting = Lighting(
                        lightId,
                        roomId,
                        lightId,
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

    override suspend fun observeChangeOnRoom202(onSuccess: onSuccess<Room>) {

        roomsRef.addChildEventListener(object : ChildEventListener {

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

                val room = dataSnapshot.value as HashMap<*, *>
                val firebaseId = dataSnapshot.key as String
                val name = room["name"] as String
                val presence = room["presence"] as Boolean

                // Retrieve nested data
                val listLightingRef = room["listLighting"] as HashMap<*,*>
                val listLighting = mutableListOf<Lighting>()
                listLightingRef.map { entry ->
                    val lightingFirebaseId = entry.key as String
                    val lighting = entry.value as HashMap<*, *>
                    val lightingName = lighting["name"] as String
                    val turnedOn = lighting["turnedOn"] as Boolean
                    listLighting.add(Lighting(lightingFirebaseId, firebaseId, lightingName, turnedOn))
                }

                // Build new instance and send updated value
                val updatedRoom = Room(firebaseId, name, presence, listLighting)
                val position = roomsId.indexOf(firebaseId)
                roomsList[position] = updatedRoom
                onSuccess(updatedRoom)
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}

interface RoomRepository {

    suspend fun retrieveDataFromFirebase(onSuccess: onSuccess<List<Room>?>)

    suspend fun turnOffTheLight(roomId: String): Room?

    suspend fun observeChangeOnRoom202(onSuccess: onSuccess<Room>)

    companion object {
        val instance: RoomRepository by lazy {
            RoomRepositoryImpl()
        }
    }
}