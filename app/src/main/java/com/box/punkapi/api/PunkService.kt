package com.box.punkapi.api

import androidx.lifecycle.LiveData
import com.box.punkapi.model.Beer
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PunkService {
    @GET("beers?per_page=30")
    suspend fun loadBeers(@Query("page") pageTo:Int = 1):List<Beer>

    companion object{
        private const val BASE_URL = "https://api.punkapi.com/v2/"

        fun create(interceptor: okhttp3.Interceptor = Interceptor()):PunkService{
            val client = OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PunkService::class.java)
        }
    }

}