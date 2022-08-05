package com.aslan.baselibrary.exception

/**
 * 客户端本地异常
 *
 * @author Aslan
 * @date 2019/9/23
 */
open class ClientException(message: String, cause: Throwable) : Exception(message, cause)