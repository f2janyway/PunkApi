package com.box.punkapi.model


sealed class Result<out T>{
    data class Success<R>(val data: R): Result<R>()
    data class Error(val exception:Exception): Result<Nothing>()
}
