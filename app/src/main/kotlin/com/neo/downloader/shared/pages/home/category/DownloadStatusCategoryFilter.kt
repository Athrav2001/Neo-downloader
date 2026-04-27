package com.neo.downloader.shared.pages.home.category

import com.neo.downloader.monitor.IDownloadItemState
import ir.neo.util.compose.IconSource
import ir.neo.util.compose.StringSource

abstract class DownloadStatusCategoryFilter(
    val name: StringSource,
    val icon: IconSource,
) {
    abstract fun accept(iDownloadStatus: IDownloadItemState): Boolean
}
