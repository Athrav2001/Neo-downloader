package com.neo.downloader.shared.util.downloadlocation

import com.neo.downloader.shared.util.SystemDownloadLocationProvider
import ir.amirab.util.platform.Platform
import ir.amirab.util.platform.asDesktop

actual fun getPlatformDownloadLocationProvider(): SystemDownloadLocationProvider {
    return when (Platform.asDesktop()) {
        Platform.Desktop.Windows -> WindowsDownloadLocationProvider()
        Platform.Desktop.Linux -> LinuxDownloadLocationProvider()
        Platform.Desktop.MacOS -> MacDownloadLocationProvider()
    }
}
