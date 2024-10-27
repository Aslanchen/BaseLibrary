package com.aslan.app.data.remote

import android.content.Context
import com.aslan.app.BuildConfig
import com.aslan.app.data.DataSource
import com.aslan.app.model.User
import com.aslan.baselibrary.http.HTTPManager
import com.aslan.baselibrary.http.SSLSocketClient
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DataRemoteDataSource(private val mContext: Context) : DataSource {
    private val mLogger = XLog.tag(HTTPManager.TAG_LOG).build()

    private val mGson = Gson()
    private var dataInterface = createHttpService(BuildConfig.HOST, 12)

    private fun createHttpService(url: String, timeout: Long): HttpService {
        val logger =
            HttpLoggingInterceptor.Logger { message -> mLogger.d(message) }
        val logging = HttpLoggingInterceptor(logger)
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }

        var okHttpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            //为了给Fiddler抓包
            okHttpClientBuilder =
                okHttpClientBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
        }

        val okHttpClient = okHttpClientBuilder
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(AuthenticatorInterceptor(mContext))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(HttpService::class.java)
    }

    override fun httpAPI1(): Maybe<User> {
        return dataInterface.httpAPI1()
            .compose(HttpDataTransformer(mContext, true))
    }

    override fun httpAPI2(): Maybe<List<User>> {
        return dataInterface.httpAPI2()
            .compose(HttpDataTransformer(mContext, true))
    }

    override fun httpAPI3(): Completable {
        return dataInterface.httpAPI3()
            .compose(HttpDataTransformer(mContext, true))
            .ignoreElement()
    }
}