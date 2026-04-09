package com.neo.downloader.android.ui.configurable.comon

import com.neo.downloader.android.ui.configurable.comon.renderer.BooleanConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.DayOfWeekConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.EnumConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.FileChecksumConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.FloatConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.FolderConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.IntConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.LongConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.NavigatableConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.ProxyConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.SpeedLimitConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.StringConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.ThemeConfigurableRenderer
import com.neo.downloader.android.ui.configurable.comon.renderer.TimeConfigurableRenderer
import com.neo.downloader.shared.ui.configurable.CommonConfigurableRenderers

val CommonConfigurableRenderersForAndroid = CommonConfigurableRenderers(
    booleanConfigurableRenderer = BooleanConfigurableRenderer,
    dayOfWeekConfigurableRenderer = DayOfWeekConfigurableRenderer,
    fileChecksumConfigurableRenderer = FileChecksumConfigurableRenderer,
    floatConfigurableRenderer = FloatConfigurableRenderer,
    folderConfigurableRenderer = FolderConfigurableRenderer,
    intConfigurableRenderer = IntConfigurableRenderer,
    longConfigurableRenderer = LongConfigurableRenderer,
    perHostSettingsConfigurableRenderer = NavigatableConfigurableRenderer,
    enumConfigurableRenderer = EnumConfigurableRenderer,
    speedConfigurableRenderer = SpeedLimitConfigurableRenderer,
    stringConfigurableRenderer = StringConfigurableRenderer,
    themeConfigurableRenderer = ThemeConfigurableRenderer,
    timeConfigurableRenderer = TimeConfigurableRenderer,
    proxyConfigurableRenderer = ProxyConfigurableRenderer,
)
