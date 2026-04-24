package com.neo.downloader.android.pages.editdownload

import com.neo.downloader.shared.downloaderinui.DownloaderInUiRegistry
import com.neo.downloader.shared.pages.editdownload.BaseEditDownloadComponent
import com.neo.downloader.shared.util.mvi.ContainsEffects
import com.neo.downloader.shared.util.mvi.supportEffects
import com.neo.downloader.shared.util.DownloadSystem
import com.neo.downloader.shared.util.FileIconProvider
import com.arkivanov.decompose.ComponentContext
import com.neo.downloader.downloaditem.DownloadJobExtraConfig
import com.neo.downloader.downloaditem.IDownloadItem
import kotlinx.coroutines.flow.*

class AndroidEditDownloadComponent(
    ctx: ComponentContext,
    onRequestClose: () -> Unit,
    downloadId: Long,
    acceptEdit: StateFlow<Boolean>,
    onEdited: ((IDownloadItem) -> Unit, DownloadJobExtraConfig?) -> Unit,
    downloadSystem: DownloadSystem,
    downloaderInUiRegistry: DownloaderInUiRegistry,
    iconProvider: FileIconProvider,
) : BaseEditDownloadComponent(
    ctx = ctx,
    downloadSystem = downloadSystem,
    downloaderInUiRegistry = downloaderInUiRegistry,
    iconProvider = iconProvider,
    onEdited = onEdited,
    onRequestClose = onRequestClose,
    downloadId = downloadId,
    acceptEdit = acceptEdit,
),
    ContainsEffects<AndroidEditDownloadComponent.Effects> by supportEffects() {
    sealed interface Effects {
    }
}
