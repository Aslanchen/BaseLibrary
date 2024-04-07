package com.aslan.baselibrary.event

import android.net.Uri

/**
 * @author chenhengfei(Aslanchen)
 * @date 2021/5/17
 */
class EventDownload(val downloadId: Long, val mUri: Uri, val mimeType: String) {
}