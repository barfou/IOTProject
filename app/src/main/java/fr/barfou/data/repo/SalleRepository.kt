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
        var listEclairage1 = mutableListOf<Eclairage>()
        listEclairage1.add(Eclairage(1, 1, "Tableau", false))
        listEclairage1.add(Eclairage(1, 1, "Classe", false))
        listSalle.add(Salle(etablissement_id = 1, id = 1, nom = "2.02", presence = true, eclairage = listEclairage1))

        var listEclairage2 = mutableListOf<Eclairage>()
        listEclairage2.add(Eclairage(1, 1, "Tableau", true))
        listEclairage2.add(Eclairage(1, 1, "Classe", false))
        listSalle.add(Salle(etablissement_id = 1, id = 2, nom = "0.09", presence = false, eclairage = listEclairage2))
        var listEclairage3 = mutableListOf<Eclairage>()
        listEclairage3.add(Eclairage(1, 1, "Tableau", true))
        listEclairage3.add(Eclairage(1, 1, "Rangée 1", true))
        listEclairage3.add(Eclairage(1, 1, "Rangée 2", true))
        listSalle.add(Salle(etablissement_id = 1, id = 3, nom = "0.08", presence = true, eclairage = listEclairage3))
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