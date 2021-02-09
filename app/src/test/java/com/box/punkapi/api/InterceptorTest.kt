package com.box.punkapi.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InterceptorTest {

    fun getRemainOverInterceptor(isOver: Boolean = false): Interceptor {
        return Interceptor {
            val req = it.request()

            assertThat(req.isHttps).isTrue()
            val res = it.proceed(req)
            val remainLimit = res.header("x-ratelimit-remaining")
            println(res.headers())
            assertThat(remainLimit).isNotEmpty()
            println(remainLimit)
            if (isOver) {
                res.apply {
                    res.header(com.box.punkapi.api.Interceptor.IS_LIMIT_OK, "true")
                }
            } else {
                res.apply {
                    res.header(com.box.punkapi.api.Interceptor.IS_LIMIT_OK, "false")
                }
            }
        }
    }

    val service = PunkService.create(getRemainOverInterceptor())
    val limitOverService = PunkService.create(getRemainOverInterceptor(true))

    @Test
    fun `interceptor header test`(): Unit = runBlocking {
        service.loadBeers()
    }

    //suspend 말고 Call<>사용시
    @Test
    fun `intercepter remain over test`(): Unit = runBlocking {
        limitOverService.loadBeers()
    }
}