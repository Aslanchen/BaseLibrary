package com.aslan.baselibrary.base

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import com.aslan.baselibrary.event.EventDownload
import com.aslan.baselibrary.utils.Config
import com.tencent.mmkv.MMKV
import org.greenrobot.eventbus.EventBus

/**
 * DownloadManager 下载广播
 */
class DownloadCompleteReceiver : BroadcastReceiver() {
    @SuppressLint("Range")
    private fun dispatchDownId(context: Context, id: Long) {
        val mDownloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
            .setFilterById(id)
            .setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)
        var cursor: Cursor? = null
        try {
            cursor = mDownloadManager.query(query)
            if (cursor == null) {
                return
            }

            if (cursor.moveToFirst()) {
                val downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE))
                val attachmentUri = Uri.parse(downloadLocalUri)
                EventBus.getDefault().post(EventDownload(attachmentUri, downloadMimeType))
            }
        } finally {
            cursor?.close()
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            val mDownloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val mmkv = MMKV.defaultMMKV()
            val downloadIdLocal = mmkv.decodeLong(Config.TAG_DOWNLOADID, -1)
            if (downloadIdLocal != downloadId) {
                return
            }

            mmkv.remove(Config.TAG_DOWNLOADID)
            dispatchDownId(context, downloadId)
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

        }
    }
}