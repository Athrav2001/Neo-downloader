package com.neo.downloader.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.neo.downloader.android.ui.configurable.comon.CommonConfigurableRenderersForAndroid
import com.neo.downloader.android.ui.configurable.comon.ConfigurableRenderersForAndroid
import com.neo.downloader.android.util.AppInfo
import com.neo.downloader.shared.repository.BaseAppRepository
import com.neo.downloader.shared.storage.BaseAppSettingsStorage
import com.neo.downloader.shared.ui.ProvideCommonSettings
import com.neo.downloader.shared.ui.ProvideSizeUnits
import com.neo.downloader.shared.ui.configurable.ConfigurableRendererRegistry
import com.neo.downloader.shared.ui.theme.NeoDownloaderTheme
import com.neo.downloader.shared.ui.theme.ThemeManager
import com.neo.downloader.shared.ui.widget.NotificationManager
import com.neo.downloader.shared.ui.widget.ProvideLanguageManager
import com.neo.downloader.shared.ui.widget.ProvideNotificationManager
import com.neo.downloader.shared.util.PopUpContainer
import com.neo.downloader.shared.util.ResponsiveBox
import com.neo.downloader.shared.util.ui.ProvideDebugInfo
import ir.neo.util.compose.IIconResolver
import ir.neo.util.compose.localizationmanager.LanguageManager
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun NeoDownloaderApplicationContent(
    languageManager: LanguageManager,
    themeManager: ThemeManager,
    appSettingsStorage: BaseAppSettingsStorage,
    iconResolver: IIconResolver,
    appRepository: BaseAppRepository,
    notificationManager: NotificationManager,
    content: @Composable () -> Unit,
) {
    val configurableRendererRegistry = remember {
        ConfigurableRendererRegistry {
            listOf(
                CommonConfigurableRenderersForAndroid,
                ConfigurableRenderersForAndroid
            ).forEach {
                it.getAllRenderers().forEach { (key, renderer) ->
                    this.register(key, renderer)
                }
            }
        }
    }
    ProvideDebugInfo(AppInfo.isInDebugMode) {
        ProvideLanguageManager(languageManager) {
            ProvideCommonSettings(
                appSettings = appSettingsStorage,
                iconProvider = iconResolver,
                configurableRendererRegistry = configurableRendererRegistry,
            ) {
                ProvideNotificationManager(notificationManager) {
                    val myColors by themeManager.currentThemeColor.collectAsState()
                    val uiScale by appSettingsStorage.uiScale.collectAsState()
                    NeoDownloaderTheme(
                        myColors = myColors,
                        fontFamily = null,
                        uiScale = uiScale,
                    ) {
                        ResponsiveBox {
                            ProvideSizeUnits(
                                appRepository
                            ) {
                                PopUpContainer {
                                    content()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
