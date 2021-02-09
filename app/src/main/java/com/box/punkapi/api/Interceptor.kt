package com.box.punkapi.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class Interceptor : Interceptor {
    private lateinit var response: Response
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        println(req)
        //..logging if need
        response = chain.proceed(req)
        return response
//        return if(isLimitOk()){
//            response.apply {
//                response.header(IS_LIMIT_OK,"true")
//            }
//        }else{
//            response.apply {
//                response.header(IS_LIMIT_OK,"false")
//            }
//        }
    }

    private fun isLimitOk(): Boolean {
        return if (this::response.isInitialized) {
            val remainCnt = response.header("x-ratelimit-remaining")?.toIntOrNull()
            remainCnt ?: 0 > 0
        } else false
    }
    companion object{
        const val IS_LIMIT_OK = "is-limit-ok"
    }
}