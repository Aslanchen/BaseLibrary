package com.aslan.app.base.data

import android.content.Context
import com.aslan.baselibrary.http.observer.DataObserver

/**
 * 封装数据流，以便统一做额外处理
 *
 * @author Aslan
 * @date 2024/10/27
 */
abstract class MyDataObserver<T : Any>(private val context: Context) : DataObserver<T>(context) {

}