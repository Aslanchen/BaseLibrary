package com.aslan.app.data.remote

import android.content.Context
import com.aslan.app.control.UserManager
import com.aslan.app.event.EventLogout
import com.aslan.baselibrary.exception.BusinessException
import com.aslan.baselibrary.exception.ClientException
import com.aslan.baselibrary.exception.TokenException
import io.reactivex.Flowable
import org.greenrobot.eventbus.EventBus
import org.reactivestreams.Publisher
import retrofit2.HttpException

/**
 * Token鉴权异常拦截处理
 */
open class TokenErrorFunction(val context: Context) :
    io.reactivex.functions.Function<Flowable<Throwable>, Publisher<Any>> {

    override fun apply(t: Flowable<Throwable>): Publisher<Any> {
        return t.flatMap {
            if (it is TokenException) {
                UserManager.onLogout()
                EventBus.getDefault().post(EventLogout(false))

//                val intent = ActivityLoginByAccount.newIntent(context, account = null)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context.startActivity(intent)
            } else if (it is ClientException) {
                if (it.cause is retrofit2.HttpException) {
                    //http异常
                    if ((it.cause as HttpException).code() == 401) {
                        UserManager.onLogout()
                        EventBus.getDefault().post(EventLogout(false))

//                        val intent = ActivityLoginByAccount.newIntent(context, account = null)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        context.startActivity(intent)

                        return@flatMap Flowable.error(BusinessException("401", "请登录后操作"))
                    }
                }
            } else if (it is BusinessException) {
                //业务异常
            }
            return@flatMap Flowable.error(it)
        }
    }
}