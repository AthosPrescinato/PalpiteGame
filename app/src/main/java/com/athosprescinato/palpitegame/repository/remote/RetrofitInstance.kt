package com.athosprescinato.palpitegame.repository.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {

        Retrofit.Builder()
            .baseUrl("https://us-central1-ss-devops.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val api: ValorService by lazy {
        retrofit.create(ValorService::class.java)
    }

}