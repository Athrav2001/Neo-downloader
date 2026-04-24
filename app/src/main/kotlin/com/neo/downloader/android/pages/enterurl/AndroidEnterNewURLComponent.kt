package com.neo.downloader.android.pages.enterurl

import com.neo.downloader.shared.downloaderinui.DownloaderInUiRegistry
import com.neo.downloader.shared.pages.enterurl.BaseEnterNewURLComponent
import com.arkivanov.decompose.ComponentContext
import com.neo.downloader.downloaditem.IDownloadCredentials

class AndroidEnterNewURLComponent(
    ctx: ComponentContext,
    config: AndroidEnterNewURLComponent.Config,
    downloaderInUiRegistry: DownloaderInUiRegistry,
    onCloseRequest: () -> Unit,
    onRequestFinished: (IDownloadCredentials) -> Unit,
) : BaseEnterNewURLComponent(
    ctx = ctx,
    config = config,
    downloaderInUiRegistry = downloaderInUiRegistry,
    onCloseRequest = onCloseRequest,
    onRequestFinished = onRequestFinished,
) {
    object Config : BaseEnterNewURLComponent.Config

    override val shouldFillWithClipboard: Boolean = false
}
