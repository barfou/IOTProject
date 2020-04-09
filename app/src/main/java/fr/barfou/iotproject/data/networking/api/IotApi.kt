package fr.barfou.iotproject.data.networking.api

import fr.barfou.iotproject.data.model.Salle
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IotApi {

    @GET(GET_ALL_SALLE_PATH)
    suspend fun getAllSalles(
        @Query("etablissement_id") id: Int
    ): Response<List<Salle>>

    companion object {
        const val GET_ALL_SALLE_PATH = "undefined"
    }
}