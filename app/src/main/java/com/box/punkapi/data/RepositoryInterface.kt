package com.box.punkapi.data

import com.box.punkapi.model.Beer
import com.box.punkapi.model.Result

interface RepositoryInterface{
    suspend fun load(pageTo: Int): Result<List<Beer>>
    suspend fun resetBeerList()
}