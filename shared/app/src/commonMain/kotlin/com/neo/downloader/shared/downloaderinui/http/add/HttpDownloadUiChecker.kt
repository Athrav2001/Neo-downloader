package com.neo.downloader.shared.downloaderinui.http.add

import com.neo.downloader.shared.downloaderinui.DownloadSize
import com.neo.downloader.shared.util.DownloadSystem
import com.neo.downloader.shared.downloaderinui.DownloadUiChecker
import com.neo.downloader.shared.downloaderinui.LinkCheckerFactory
import ir.amirab.downloader.connection.response.HttpResponseInfo
import ir.amirab.downloader.downloaditem.http.HttpDownloadCredentials
import kotlinx.coroutines.CoroutineScope

class HttpDownloadUiChecker(
    initialCredentials: HttpDownloadCredentials = HttpDownloadCredentials.Companion.empty(),
    linkCheckerFactory: LinkCheckerFactory<HttpDownloadCredentials, HttpResponseInfo, DownloadSize.Bytes, HttpLinkChecker>,
    initialFolder: String,
    initialName: String = "",
    downloadSystem: DownloadSystem,
    scope: CoroutineScope,
) : DownloadUiChecker<HttpDownloadCredentials, HttpResponseInfo, DownloadSize.Bytes, HttpLinkChecker>(
    initialCredentials, linkCheckerFactory, initialFolder, initialName, downloadSystem, scope
) {
}
