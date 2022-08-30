package com.aslan.baselibrary.exception

/**
 * 业务服务器异常
 *
 * @author Aslan
 * @date 2019/9/23
 */
open class BusinessException(val code: String, message: String) : Exception(message)