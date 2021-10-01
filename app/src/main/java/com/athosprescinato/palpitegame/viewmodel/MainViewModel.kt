package com.athosprescinato.palpitegame.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athosprescinato.palpitegame.model.ValorModel
import com.athosprescinato.palpitegame.repository.ValorRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: ValorRepository): ViewModel() {

    val myResponse: MutableLiveData<Response<ValorModel>> = MutableLiveData()

    fun getValor() {
        viewModelScope.launch {
            val response = repository.getValor()
            myResponse.value = response
        }
    }
}