package com.box.punkapi.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.box.punkapi.api.PunkService
import com.box.punkapi.data.BeerRepository
import com.box.punkapi.ui.MainViewModel

object Injector {
    private fun provideRepository(): BeerRepository = BeerRepository(PunkService.create())

    fun provideViewModel():ViewModelProvider.Factory = ViewModelFactory(provideRepository())
}

class ViewModelFactory(private val repository: BeerRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository = repository) as T
    }
}