package com.neo.downloader.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.neo.downloader.android.pages.onboarding.permissions.PermissionManager
import com.neo.downloader.android.util.NDMAppManager
import com.neo.downloader.shared.storage.BaseAppSettingsStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StartOnBootBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val appManager: NDMAppManager by inject()
    private val appSettingStorage: BaseAppSettingsStorage by inject()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (appSettingStorage.autoStartOnBoot.value) {
                appManager.bootDownloadSystemAndService()
            }
        }
    }
}
