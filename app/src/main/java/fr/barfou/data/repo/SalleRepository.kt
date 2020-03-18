package fr.barfou.data.repo

import fr.barfou.data.model.Eclairage
import fr.barfou.data.model.Salle
import fr.barfou.data.networking.HttpClientManager
import fr.barfou.data.networking.api.IotApi
import fr.barfou.data.networking.createApi

class SalleRepositoryImpl(
    //private val iotApi: IotApi
): SalleRepository {

    override suspend fun getAllSalles(etablissement_id: Int): List<Salle> {
        // Test Values
        var listSalle = mutableListOf<Salle>()
        listSalle.add(Salle(etablissement_id = 1, id = 1, nom = "Living Room", presence = true, eclairage = emptyList()))
        var listEclairage = mutableListOf<Eclairage>()
        listEclairage.add(Eclairage(1, 1, "E1", true))
        listSalle.add(Salle(etablissement_id = 1, id = 2, nom = "BedRoom", presence = false, eclairage = listEclairage))
        listSalle.add(Salle(etablissement_id = 1, id = 3, nom = "Kitchen", presence = true, eclairage = listEclairage))
        return listSalle
    }
}

interface SalleRepository {

    suspend fun getAllSalles(etablissement_id: Int): List<Salle>

    companion object {
        val instance: SalleRepository by lazy {
            SalleRepositoryImpl()
            //SalleRepositoryImpl(HttpClientManager.instance.createApi())
        }
    }
}