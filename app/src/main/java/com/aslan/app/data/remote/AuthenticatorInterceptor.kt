package com.aslan.app.data.remote

import android.content.Context
import com.aslan.app.control.UserManager
import com.aslan.baselibrary.http.HTTPManager
import com.elvishew.xlog.XLog
import okhttp3.Interceptor
import okhttp3.Response

open class AuthenticatorInterceptor(var mContext: Context) : Interceptor {
    private val mLogger = XLog.tag(HTTPManager.TAG_LOG).build()
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = UserManager.getUser().token
        if (token.isNullOrBlank()) {
            return chain.proceed(request)
        }

        mLogger.d("Token= " + token)
        val newRequest = request.newBuilder()
            .header("token", token)
            .build()
        return chain.proceed(newRequest)
    }
}