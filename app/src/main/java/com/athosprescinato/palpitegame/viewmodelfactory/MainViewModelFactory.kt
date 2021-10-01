package com.athosprescinato.palpitegame.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.athosprescinato.palpitegame.repository.ValorRepository
import com.athosprescinato.palpitegame.viewmodel.MainViewModel

class MainViewModelFactory(private val repository: ValorRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}