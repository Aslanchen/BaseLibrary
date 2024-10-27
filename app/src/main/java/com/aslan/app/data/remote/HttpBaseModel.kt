package com.aslan.app.data.remote

import com.aslan.baselibrary.http.IHttpBean

class HttpBaseModel<T> : IHttpBean<T> {
    private var code = 0
    private var data: T? = null
    private var msg: String? = null

    override fun getMessage(): String = msg ?: ""

    override fun isTokenError() = (code == 401)

    override fun isSuccessful() = (code == 1)

    override fun getCode() = code.toString()

    override fun getData() = data
}