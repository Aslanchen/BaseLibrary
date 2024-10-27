package com.aslan.app.data

import com.aslan.app.model.User
import io.reactivex.Completable
import io.reactivex.Maybe

/**
 * App业务
 */
interface DataSource {
    fun httpAPI1(): Maybe<User>
    fun httpAPI2(): Maybe<List<User>>
    fun httpAPI3(): Completable
}