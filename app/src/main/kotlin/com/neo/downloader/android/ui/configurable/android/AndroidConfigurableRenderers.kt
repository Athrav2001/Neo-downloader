package com.neo.downloader.android.ui.configurable.android

import com.neo.downloader.android.ui.configurable.android.item.PermissionConfigurable
import com.neo.downloader.shared.ui.configurable.Configurable
import com.neo.downloader.shared.ui.configurable.ConfigurableRenderer
import com.neo.downloader.shared.ui.configurable.ContainsConfigurableRenderers

data class AndroidConfigurableRenderers(
    val permissionConfigurableRenderers: ConfigurableRenderer<PermissionConfigurable>,
) : ContainsConfigurableRenderers {
    override fun getAllRenderers(): Map<Configurable.Key, ConfigurableRenderer<*>> {
        return mapOf(
            PermissionConfigurable.Key to permissionConfigurableRenderers,
        )
    }
}
