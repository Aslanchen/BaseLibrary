package com.aslan.app.data.remote

import com.aslan.app.model.User
import io.reactivex.Maybe
import retrofit2.http.POST

interface HttpService {
    @POST("XXX/XXXX")
    fun httpAPI1(): Maybe<HttpBaseModel<User>>

    @POST("XXX/XXXX")
    fun httpAPI2(): Maybe<HttpBaseModel<List<User>>>

    @POST("XXX/XXXX")
    fun httpAPI3(): Maybe<HttpBaseModel<Any>>
}