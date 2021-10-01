package com.athosprescinato.palpitegame.repository

import com.athosprescinato.palpitegame.model.ValorModel
import com.athosprescinato.palpitegame.repository.remote.RetrofitInstance
import retrofit2.Response

class ValorRepository {

    suspend fun getValor() : Response<ValorModel> {
        return RetrofitInstance.api.getValor()

    }

}