package com.neo.downloader.android

import android.app.Application
import com.neo.downloader.android.di.Di
import com.neo.downloader.android.pages.onboarding.permissions.PermissionManager
import com.neo.downloader.android.util.NDMAppManager
import com.neo.downloader.android.util.AndroidGlobalExceptionHandler
import com.neo.downloader.android.util.AppInfo
import com.neo.downloader.android.util.ApplicationBackgroundTracker
import com.neo.downloader.shared.repository.BaseAppRepository
import com.neo.downloader.shared.util.appinfo.PreviousVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NDMApp : Application(), KoinComponent {
    val TAG_NAME = NDMApp::class.simpleName!!
    val appManager: NDMAppManager by inject()
    val appRepository: BaseAppRepository by inject()
    val previousVersion: PreviousVersion by inject()
    val scope: CoroutineScope by inject()
    override fun onCreate() {
        super.onCreate()
        AppInfo.init(this)
        Di.boot(this)
        ApplicationBackgroundTracker.startTracking(this)
        appRepository.boot()
        previousVersion.boot()
        Thread.setDefaultUncaughtExceptionHandler(
            AndroidGlobalExceptionHandler(
                this,
                Thread.getDefaultUncaughtExceptionHandler(),
            )
        )
        appManager.boot()
    }
}
