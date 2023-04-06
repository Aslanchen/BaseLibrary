package com.aslan.baselibrary.http.response

import io.reactivex.Single
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.net.URLDecoder

/**
 * 文件下载处理类，将文件下载到指定目录，如果文件名为空，则从响应头中获取文件名
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
                val arrays = contentDisposition.split(";")
                val maps = hashMapOf<String, String>()
                arrays.forEach {
                    if ("=" in it) {
                        val values = it.split("=")
                        if (values.size == 2) {
                            maps[values[0].trim()] = values[1].trim()
                        }
                    }
                }

                val name = if (maps.containsKey("filename*")) {
                    val values = maps["filename*"]?.split("''")
                    if (values.isNullOrEmpty() || values.size < 2) {
                        null
                    } else {
                        URLDecoder.decode(values[1].trim(), values[0].trim())
                    }
                } else if (maps.containsKey("filename")) {
                    maps["filename"]?.replace("\"", "")?.replace("'", "")
                } else {
                    null
                }

                if (name.isNullOrEmpty()) {
                    return Single.error(Exception("文件名称未指定"))
                }
                File(root, name)
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