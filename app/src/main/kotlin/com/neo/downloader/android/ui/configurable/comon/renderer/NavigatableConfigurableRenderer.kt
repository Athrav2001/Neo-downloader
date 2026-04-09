package com.neo.downloader.android.ui.configurable.comon.renderer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neo.downloader.android.ui.configurable.ConfigTemplate
import com.neo.downloader.android.ui.configurable.NextIcon
import com.neo.downloader.android.ui.configurable.TitleAndDescription
import com.neo.downloader.shared.ui.configurable.ConfigurableRenderer
import com.neo.downloader.shared.ui.configurable.ConfigurableUiProps
import com.neo.downloader.shared.ui.configurable.item.NavigatableConfigurable
import com.neo.downloader.shared.util.ui.icon.MyIcons
import com.neo.downloader.shared.util.ui.widget.MyIcon

object NavigatableConfigurableRenderer : ConfigurableRenderer<NavigatableConfigurable> {
    @Composable
    override fun RenderConfigurable(
        configurable: NavigatableConfigurable,
        configurableUiProps: ConfigurableUiProps
    ) {
        RenderPerHostSettingsConfigurable(
            cfg = configurable,
            configurableUiProps = configurableUiProps,
            onRequestOpenConfigWindow = configurable.onRequestNavigate,
        )
    }

    @Composable
    private fun RenderPerHostSettingsConfigurable(
        cfg: NavigatableConfigurable,
        configurableUiProps: ConfigurableUiProps,
        onRequestOpenConfigWindow: () -> Unit
    ) {
//    val value by cfg.stateFlow.collectAsState()
//    val setValue = cfg::set
//    val enabled = isConfigEnabled()

        ConfigTemplate(
            modifier = configurableUiProps.modifier
                .clickable {
                    onRequestOpenConfigWindow()
                }
                .padding(configurableUiProps.itemPaddingValues),
            title = {
                TitleAndDescription(cfg, true)
            },
            value = {
                NextIcon()
            },
        )
    }

}
