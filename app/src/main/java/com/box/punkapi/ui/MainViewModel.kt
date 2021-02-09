package com.box.punkapi.ui

import androidx.lifecycle.*
import com.box.punkapi.data.BeerRepository
import com.box.punkapi.model.Beer
import com.box.punkapi.model.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BeerRepository) : ViewModel() {

    private val _loadedBeers = MutableLiveData<Result<List<Beer>>>()
    val loadedBeers: LiveData<Result<List<Beer>>>
        get() = _loadedBeers


    private lateinit var loadJob: Job
    lateinit var selectedBeer: Beer
        private set
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    fun load(pageTo: Int) {
        if(this::loadJob.isInitialized){
            if (!loadJob.isCompleted) return
        }
        _isLoading.value = true

        loadJob = viewModelScope.launch {
            _loadedBeers.value = repository.load(pageTo)
            _isLoading.value = false
        }
    }

    fun selectBeer(beer: Beer) {
        selectedBeer = beer
    }
}