package com.aslan.baselibrary.exception

/**
 * 业务服务器异常
 *
 * @author Aslan
 * @date 2019/9/23
 */
open class BusinessException(open var code: String, open override var message: String) : Exception(message)