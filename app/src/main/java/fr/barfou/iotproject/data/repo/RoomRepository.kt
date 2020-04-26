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
    private val lightingNames = listOf("Classe", "Tableau")

    override suspend fun turnOffTheLight(roomId: String): Room? {
        return withContext(Dispatchers.IO) {
            try {
                val position = roomId.indexOf(roomId)
                val updatedRoom = roomsList[position]
                updatedRoom.switchOffTheLight()
                roomsRef.child(roomId).setValue(updatedRoom)
                roomsList[position] = updatedRoom
                return@withContext updatedRoom
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
            var temp = mutableListOf<Room>()
            taskMap.map { entry ->
                val room = entry.value as HashMap<*, *>
                val firebaseId = entry.key as String
                val nom = room["name"] as String
                val presence = room["presence"] as Boolean
                val newRoom = Room(firebaseId, nom, presence, mutableListOf())
                if (firebaseId == roomsId[0])
                    roomsList.add(newRoom)
                else
                    temp.add(newRoom)
            }
            roomsList.addAll(temp)
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
            roomsId.forEachIndexed { index, id ->
                var roomName = id.replace("Rm", "Salle ")
                roomName = roomName.substring(0, 7) + "." + roomName.substring(7, roomName.length);
                val newRoom = Room(
                    id, roomName, nextBoolean(), mutableListOf()
                )
                roomsList.add(newRoom)
                roomsRef.child(id).setValue(newRoom)
                for (name in lightingNames) {
                    val lightingRef = roomsRef.child(id).child("listLighting")
                    val lightId = lightingRef.push().key!!
                    val lighting = Lighting(
                        lightId, id,
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

    override suspend fun observeChangeOnRoom202(onSuccess: onSuccess<Boolean>) {

        //val room202Ref = roomsRef.child(roomsId[0])

        roomsRef.addChildEventListener(object : ChildEventListener {

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val room = dataSnapshot.value as HashMap<*, *>
                val presence = room["presence"] as Boolean
                val firebaseId = dataSnapshot.key as String
                roomsList[0].presence = presence
                if (firebaseId == roomsId[0])
                    onSuccess(presence)
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override suspend fun observeChangeOnNestedDataRoom202(onSuccess: onSuccess<MutableList<Lighting>>) {

        val lightingRef = roomsRef.child(roomsId[0]).child("listLighting")

        lightingRef.addChildEventListener(object : ChildEventListener {

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

                val listLighting = mutableListOf<Lighting>()
                val lightingMap = dataSnapshot.value as HashMap<*, *>
                lightingMap.map { entry ->
                    val firebaseId = entry.key as String
                    val lighting = entry.value as HashMap<*, *>
                    val name = lighting["name"] as String
                    val turnedOn = lighting["turnedOn"] as Boolean
                    listLighting.add(Lighting(firebaseId, roomsId[0], name, turnedOn))
                }
                roomsList[0].listLighting = listLighting
                onSuccess(listLighting)
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

    suspend fun observeChangeOnRoom202(onSuccess: onSuccess<Boolean>)

    suspend fun observeChangeOnNestedDataRoom202(onSuccess: onSuccess<MutableList<Lighting>>)

    companion object {
        val instance: RoomRepository by lazy {
            RoomRepositoryImpl()
        }
    }
}