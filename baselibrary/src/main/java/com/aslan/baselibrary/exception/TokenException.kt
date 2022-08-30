package com.aslan.baselibrary.exception

/**
 * token异常
 *
 * @author Aslan
 * @date 2019/9/23
 */
class TokenException(code: String, message: String) : BusinessException(code, message)