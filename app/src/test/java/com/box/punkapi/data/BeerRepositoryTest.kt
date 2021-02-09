package com.box.punkapi.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.box.punkapi.api.PunkService
import com.box.punkapi.model.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BeerRepositoryTest{
    val respository  = BeerRepository(PunkService.create())
    @Test
    fun `beer load test`(): Unit = runBlocking{
        val result = respository.load(1)
        assertThat(result).isInstanceOf(Result::class.java)
        when(result){
            is Result.Success->{
                println(result.data)
                assertThat(result.data).isNotEmpty()
            }
            is Result.Error->{
                throw result.exception
            }
        }
    }
}