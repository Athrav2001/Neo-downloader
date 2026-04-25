package com.neo.downloader.shared.pages.home.category

import com.neo.downloader.downloaditem.DownloadStatus
import com.neo.downloader.monitor.IDownloadItemState
import com.neo.downloader.monitor.statusOrFinished
import ir.neo.util.compose.IconSource
import ir.neo.util.compose.StringSource

class DownloadStatusCategoryFilterByList(
    name: StringSource,
    icon: IconSource,
    val acceptedStatus: List<DownloadStatus>,
) : DownloadStatusCategoryFilter(name, icon) {
    override fun accept(iDownloadStatus: IDownloadItemState): Boolean {
        return iDownloadStatus
            .statusOrFinished()
            .asDownloadStatus() in acceptedStatus
    }
}
