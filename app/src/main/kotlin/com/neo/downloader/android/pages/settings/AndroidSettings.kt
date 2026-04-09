package com.neo.downloader.android.pages.settings

import com.neo.downloader.android.pages.onboarding.permissions.NDMPermissions
import com.neo.downloader.android.storage.AppSettingsStorage
import com.neo.downloader.android.ui.configurable.android.item.PermissionConfigurable
import com.neo.downloader.android.util.pagemanager.PermissionsPageManager
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.configurable.item.BooleanConfigurable
import com.neo.downloader.shared.ui.configurable.item.NavigatableConfigurable
import ir.amirab.util.compose.asStringSource
import kotlinx.coroutines.flow.MutableStateFlow


object AndroidSettings {
    fun permissionSettings(
        permissionsPageManager: PermissionsPageManager
    ): NavigatableConfigurable {
        return NavigatableConfigurable(
            title = Res.string.permissions.asStringSource(),
            description = "".asStringSource(),
            onRequestNavigate = {
                permissionsPageManager.openPermissionsPage(false)
            },
        )
    }

    fun ignoreBatteryOptimizations(): PermissionConfigurable {
        val permission = NDMPermissions.BatteryOptimizationPermission
        return PermissionConfigurable(
            title = permission.title,
            description = permission.description,
            backedBy = MutableStateFlow(permission),
        )
    }

    fun browserIconInLauncher(
        appSettingsStorage: AppSettingsStorage
    ): BooleanConfigurable {
        return BooleanConfigurable(
            title = Res.string.settings_browser_in_launcher.asStringSource(),
            description = Res.string.settings_browser_in_launcher_description.asStringSource(),
            backedBy = appSettingsStorage.browserIconInLauncher,
            describe = {
                if (it) {
                    Res.string.enabled
                } else {
                    Res.string.disabled
                }.asStringSource()
            }
        )
    }
}
