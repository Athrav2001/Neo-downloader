package com.neo.downloader.android.ui.configurable.android.renderer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.neo.downloader.android.pages.onboarding.permissions.AppPermissionState
import com.neo.downloader.android.pages.onboarding.permissions.rememberAppPermissionState
import com.neo.downloader.android.ui.configurable.ConfigTemplate
import com.neo.downloader.android.ui.configurable.NextIcon
import com.neo.downloader.android.ui.configurable.TitleAndDescription
import com.neo.downloader.android.ui.configurable.android.item.PermissionConfigurable
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.configurable.ConfigurableRenderer
import com.neo.downloader.shared.ui.configurable.ConfigurableUiProps
import com.neo.downloader.shared.util.ui.LocalContentColor
import com.neo.downloader.shared.util.ui.myColors
import ir.neo.util.compose.asStringSource

object PermissionConfigurableRenderer : ConfigurableRenderer<PermissionConfigurable> {
    @Composable
    override fun RenderConfigurable(
        configurable: PermissionConfigurable,
        configurableUiProps: ConfigurableUiProps
    ) {
        val permission by configurable.stateFlow.collectAsState()
        val permissionState = rememberAppPermissionState(permission) { result ->

        }

        RenderPermissionConfigurable(
            cfg = configurable,
            configurableUiProps = configurableUiProps,
            permissionState = permissionState
        )
    }

    @Composable
    fun RenderPermissionConfigurable(
        cfg: PermissionConfigurable,
        configurableUiProps: ConfigurableUiProps,
        permissionState: AppPermissionState,
    ) {
        ConfigTemplate(
            modifier = configurableUiProps.modifier
                .clickable {
                    permissionState.launchRequest()
                }
                .padding(configurableUiProps.itemPaddingValues),
            title = {
                TitleAndDescription(
                    cfg = cfg,
                    describe = true,
                    describeContent = if (permissionState.isGranted) {
                        Res.string.permission_granted
                    } else {
                        Res.string.permission_not_granted
                    }.asStringSource().rememberString(),
                    describeWrapper = { content ->
                        val contentColor =
                            if (permissionState.isGranted) myColors.success else myColors.warning
                        CompositionLocalProvider(
                            LocalContentColor provides contentColor
                        ) {
                            content()
                        }
                    }
                )
            },
            value = {
                NextIcon()
            }
        )
    }
}
