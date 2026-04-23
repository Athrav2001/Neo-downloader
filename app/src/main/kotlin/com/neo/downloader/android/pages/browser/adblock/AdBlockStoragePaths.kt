package com.neo.downloader.android.pages.browser.adblock

import java.io.File

object AdBlockStoragePaths {
    private val baseDirFile = File("/storage/emulated/0/Download/NDM/.adblocker_Neo")

    fun baseDir(): File = baseDirFile

    fun sourcesFile(): File = baseDirFile.resolve("adBlockSources.json")

    fun hostsFile(): File = baseDirFile.resolve("adBlockHosts.txt")

    fun sourceHostsDir(): File = baseDirFile.resolve("adBlockSourceHosts")
}

