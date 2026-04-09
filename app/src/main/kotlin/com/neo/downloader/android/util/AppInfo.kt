package com.neo.downloader.android.util

import android.app.Application
import com.neo.downloader.android.BuildConfig
import com.neo.downloader.shared.util.AppVersion
import com.neo.downloader.shared.util.SharedConstants
import ir.amirab.util.platform.Platform
import okio.Path.Companion.toOkioPath

object AppInfo {
    val isInDebugMode: Boolean = BuildConfig.DEBUG
    lateinit var context: Application
    fun init(context: Application) {
        this.context = context
    }

    val platform = Platform.Android
    val version = AppVersion.get()

    val definedPaths by lazy {
        AndroidDefinedPaths(
            dataDir = context.filesDir.resolve(
                SharedConstants.dataDirName
            ).toOkioPath()
        )
    }
}
