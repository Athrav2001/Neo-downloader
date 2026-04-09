package com.neo.downloader.shared.util.downloadlocation

import java.io.File

class MacDownloadLocationProvider : DesktopDownloadLocationProvider() {
    override fun getCurrentDownloadLocation(): File? {
        return getCommonDownloadLocation()
    }
}
