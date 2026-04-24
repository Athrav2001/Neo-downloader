package com.neo.downloader.android.repository

import com.neo.downloader.android.pages.browser.BrowserActivity
import com.neo.downloader.android.storage.AppSettingsStorage
import com.neo.downloader.shared.repository.BaseAppRepository
import com.neo.downloader.shared.util.DownloadSystem
import com.neo.downloader.shared.util.autoremove.RemovedDownloadsFromDiskTracker
import com.neo.downloader.shared.util.category.CategoryManager
import com.neo.downloader.shared.util.proxy.ProxyManager
import com.neo.downloader.DownloadSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AppRepository(
    scope: CoroutineScope,
    appSettings: AppSettingsStorage,
    proxyManager: ProxyManager,
    downloadSystem: DownloadSystem,
    downloadSettings: DownloadSettings,
    removedDownloadsFromDiskTracker: RemovedDownloadsFromDiskTracker,
    categoryManager: CategoryManager,
) : BaseAppRepository(
    scope = scope,
    appSettings = appSettings,
    proxyManager = proxyManager,
    downloadSystem = downloadSystem,
    downloadSettings = downloadSettings,
    removedDownloadsFromDiskTracker = removedDownloadsFromDiskTracker,
    categoryManager = categoryManager,
) {
    init {
        appSettings.browserIconInLauncher
            .debounce(500)
            .distinctUntilChanged()
            .onEach { enabled ->
                BrowserActivity.Companion.Launcher.setEnabled(enabled)
            }.launchIn(scope)
    }
}
