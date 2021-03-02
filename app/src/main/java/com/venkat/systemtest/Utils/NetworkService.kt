package com.venkat.systemtest.Utils

import Items
import Json4Kotlin_Base
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("search/repositories")
    fun getNews(
        @Query("q") p: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<Json4Kotlin_Base>

}