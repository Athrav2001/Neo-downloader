package com.neo.downloader.android.util.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import com.neo.downloader.android.storage.AndroidOnBoardingStorage
import com.neo.downloader.android.storage.HomePageStorage
import com.neo.downloader.android.ui.NeoDownloaderApplicationContent
import com.neo.downloader.android.util.NDMAppManager
import com.neo.downloader.android.util.AndroidUi
import com.neo.downloader.shared.repository.BaseAppRepository
import com.neo.downloader.shared.storage.BaseAppSettingsStorage
import com.neo.downloader.shared.ui.theme.ThemeManager
import com.neo.downloader.shared.ui.widget.NotificationManager
import com.neo.downloader.shared.util.perhostsettings.PerHostSettingsManager
import com.neo.downloader.shared.util.ui.MyColors
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.retainedComponent
import ir.amirab.util.compose.IIconResolver
import ir.amirab.util.compose.localizationmanager.LanguageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

abstract class NDMActivity : ComponentActivity(), KoinComponent {
    val languageManager: LanguageManager by inject()
    val themeManager: ThemeManager by inject()
    val appSettingsStorage: BaseAppSettingsStorage by inject()
    val iconResolver: IIconResolver by inject()
    val appRepository: BaseAppRepository by inject()
    val notificationManager: NotificationManager by inject()
    val applicationScope: CoroutineScope by inject()
    val perHostSettingsManager: PerHostSettingsManager by inject()
    val ndmAppManager: NDMAppManager by inject()
    val onBoardingStorage: AndroidOnBoardingStorage by inject()
    val homePageStorage: HomePageStorage by inject()

    open fun handleIntent(intent: Intent) {}

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidUi.boot()
        val isLight = themeManager.currentThemeColor.value.isLight
        val transparent = Color.Transparent.toArgb()
        val systemBarStyle = if (isLight) {
            SystemBarStyle.light(transparent, transparent)
        } else {
            SystemBarStyle.dark(transparent)
        }
        enableEdgeToEdge(
            statusBarStyle = systemBarStyle,
            navigationBarStyle = systemBarStyle,
        )
        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        ndmAppManager.bootDownloadSystemAndService()
    }

    @Composable
    private fun UpdateSystemBarColors(
        myColors: MyColors,
    ) {
        val window = window
        val isLight = myColors.isLight
        LaunchedEffect(isLight) {
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            windowInsetsController.isAppearanceLightStatusBars = isLight
            windowInsetsController.isAppearanceLightNavigationBars = isLight
        }
    }

    fun setNDMContent(
        content: @Composable () -> Unit,
    ) {
        setContent {
            val theme by themeManager.currentThemeColor.collectAsState()
            UpdateSystemBarColors(theme)
            NeoDownloaderApplicationContent(
                languageManager = languageManager,
                themeManager = themeManager,
                appSettingsStorage = appSettingsStorage,
                iconResolver = iconResolver,
                appRepository = appRepository,
                notificationManager = notificationManager,
                content = content,
            )
        }
    }

    fun <T> myRetainedComponent(factory: RetainedComponentContainer<T>.(ComponentContext) -> T): RetainedComponentContainer<T> {
        return retainedComponent { RetainedComponentContainer(it, factory) }
            .reinitialize(this)
    }
}


