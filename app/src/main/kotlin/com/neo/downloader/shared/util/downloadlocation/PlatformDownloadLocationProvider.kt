package com.neo.downloader.shared.util.downloadlocation

import com.neo.downloader.shared.util.SystemDownloadLocationProvider

object PlatformDownloadLocationProvider {
    val instance: SystemDownloadLocationProvider by lazy {
        getPlatformDownloadLocationProvider()
    }
}

expect fun getPlatformDownloadLocationProvider(): SystemDownloadLocationProvider

