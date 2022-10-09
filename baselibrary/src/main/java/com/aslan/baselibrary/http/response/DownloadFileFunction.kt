package com.aslan.baselibrary.http.response

import io.reactivex.Single
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.util.regex.Pattern


/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class DownloadFileFunction(val root: File, val filename: String? = null) :
    Function<retrofit2.Response<ResponseBody>, Single<File>> {
    @Throws(Exception::class)
    override fun apply(response: retrofit2.Response<ResponseBody>): Single<File> {
        val headers = response.headers()
        val fileDes = if (filename.isNullOrEmpty()) {
            val contentDisposition = headers.get("Content-Disposition")
            if (contentDisposition.isNullOrEmpty()) {
                return Single.error(Exception("文件名称未指定"))
            } else {
                val pattern = Pattern.compile("filename=(.+);")
                val matcher = pattern.matcher(contentDisposition)
                if (matcher.find()) {
                    val name = matcher.group(1)
                    File(root, name)
                } else {
                    return Single.error(Exception("文件名称未指定"))
                }
            }
        } else {
            File(root, filename)
        }

        val body = response.body() ?: return Single.error(Exception("服务端数据为空"))
        return try {
            fileDes.sink().buffer().use {
                it.writeAll(body.source())
                Single.just(fileDes)
            }
        } catch (e: IOException) {
            Single.error(e)
        }
    }
}