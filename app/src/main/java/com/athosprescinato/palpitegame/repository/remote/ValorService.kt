package com.athosprescinato.palpitegame.repository.remote

import com.athosprescinato.palpitegame.model.ValorModel
import retrofit2.Response
import retrofit2.http.GET

interface ValorService {

    @GET("rand?min=1&max=300")
    suspend fun getValor(): Response<ValorModel>


}