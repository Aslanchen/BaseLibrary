package com.aslan.baselibrary.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity.DOWNLOAD_SERVICE
import java.io.File

/**
 * 下载工具类
 *
 * @author Aslan
 * @date 2018/4/11
 */
object DownloadUtil {

    /**
     * 查询当前下载器任务状态
     */
    fun getStatusForDownloadedFile(context: Context, id: Long): Int? {
        val mDownloadManager = context.getSystemService(DOWNLOAD_SERVICE) as android.app.DownloadManager
        val query = android.app.DownloadManager.Query().setFilterById(id)
        var cursor: Cursor? = null
        try {
            cursor = mDownloadManager.query(query)
            if (cursor == null) {
                return null
            }

            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_STATUS))
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * 系统系统DownloadManager实现下载，下载完成后会触发Event时间[com.aslan.baselibrary.event.EventDownload]
     */
    fun downloadByManager(context: Context, mConfig: Config): Long {
        if (mConfig.downloadId != null && mConfig.downloadId != -1L) {
            val status = getStatusForDownloadedFile(context, mConfig.downloadId)
            if (status == android.app.DownloadManager.STATUS_RUNNING) {
                throw DownloadError(context.getString(com.aslan.baselibrary.R.string.download_is_in_download))
            }
        }

        val root = FileUtil.getDownload(context)
        val file = File(root, mConfig.filename)
        LogUtils.d(file.path)
        if (file.exists()) {
            file.delete()
        }

        val mDownloadManager = context.getSystemService(DOWNLOAD_SERVICE) as android.app.DownloadManager
        val request = android.app.DownloadManager.Request(Uri.parse(mConfig.url))
            .setAllowedNetworkTypes(mConfig.allowedNetworkTypes)
            .setNotificationVisibility(mConfig.notificationVisibility)
            .setTitle(mConfig.title)
            .setDescription(mConfig.description)
            .setDestinationInExternalFilesDir(context, mConfig.dirType, file.name)
        return mDownloadManager.enqueue(request)
    }

    /**
     * 通过系统浏览器下载
     */
    fun downloadByBrowser(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(uri)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"))
        } catch (ex: ActivityNotFoundException) {
            throw DownloadError("没有匹配的程序")
        }
    }

    open class DownloadError(message: String) : Throwable(message)

    open class Config(
        val downloadId: Long? = -1,
        val url: String,
        val dirType: String = Environment.DIRECTORY_DOWNLOADS,
        val filename: String,
        val title: String,
        val description: String,
        val allowedNetworkTypes: Int = android.app.DownloadManager.Request.NETWORK_WIFI or android.app.DownloadManager.Request.NETWORK_MOBILE,
        val notificationVisibility: Int = android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
    )
}
