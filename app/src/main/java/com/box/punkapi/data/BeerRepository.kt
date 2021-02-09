package com.box.punkapi.data

import androidx.lifecycle.LiveData
import com.box.punkapi.api.PunkService
import com.box.punkapi.model.Beer
import com.box.punkapi.model.Result
import retrofit2.HttpException
import java.io.IOException

class BeerRepository(private val service: PunkService) : RepositoryInterface {
    private val beerList = arrayListOf<Beer>()
    /**
     * suspend api call이 많을 경우
     * retrofit call wrapper adapter를 만들어 주면 편하겠습니다.
     * [참고](https://f2janyway.github.io/android/kotlin/retrofit-suspend-callback/)
     */
    override suspend fun load(pageTo: Int): Result<List<Beer>> {
        return try {
            beerList.addAll(service.loadBeers(pageTo))
            Result.Success(beerList)
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun resetBeerList() {
        beerList.clear()
    }
}