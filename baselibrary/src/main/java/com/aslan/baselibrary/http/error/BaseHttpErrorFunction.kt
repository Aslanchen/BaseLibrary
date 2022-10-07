package com.aslan.baselibrary.http.error

import android.content.Context
import android.net.ParseException
import android.util.Log
import com.aslan.baselibrary.R
import com.aslan.baselibrary.exception.BusinessException
import com.aslan.baselibrary.exception.ClientException
import com.aslan.baselibrary.http.NetManager
import com.google.gson.JsonParseException
import io.reactivex.functions.Function
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException

open abstract class BaseHttpErrorFunction<T>(val context: Context) : Function<Throwable, T> {

    override fun apply(t: Throwable): T {
        val ex = when (t) {
            is UnknownHostException -> ClientException(
                context.getString(R.string.error_net_no_net), t
            )
            is ConnectException -> ClientException(
                context.getString(R.string.error_net), t
            )
            is ConnectTimeoutException -> ClientException(
                context.getString(R.string.error_net_connect_timeout), t
            )
            is SocketTimeoutException -> ClientException(
                context.getString(R.string.error_net_socket_timeout), t
            )
            is retrofit2.HttpException -> ClientException(
                context.getString(R.string.error_net), t
            )
            is JsonParseException, is JSONException, is ParseException -> ClientException(
                context.getString(R.string.error_data_parse), t
            )
            is SQLException -> ClientException(
                context.getString(R.string.error_local_database_default), t
            )
            is ClientException -> t
            is BusinessException -> t
            else -> ClientException(context.getString(R.string.error_unknow), t)
        }

        if (ex is ClientException) {
            Log.e(NetManager.TAG_LOG, "ClientException", ex)
        } else if (ex is BusinessException) {
            Log.e(
                NetManager.TAG_LOG,
                String.format("BusinessException code= [%s] msg= [%s]", ex.code, ex.message)
            )
        } else {
            Log.e(NetManager.TAG_LOG, "Unknow exception", t)
        }
        return error(ex)
    }

    abstract fun error(ex: Exception): T
}