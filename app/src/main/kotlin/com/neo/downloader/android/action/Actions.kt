package com.neo.downloader.android.action

import com.neo.downloader.android.util.pagemanager.IBrowserPageManager
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.util.ui.icon.MyIcons
import ir.amirab.util.compose.action.AnAction
import ir.amirab.util.compose.action.simpleAction
import ir.amirab.util.compose.asStringSource

fun createOpenBrowserAction(
    browserPageManager: IBrowserPageManager,
): AnAction {
    return simpleAction(
        Res.string.browser.asStringSource(),
        MyIcons.earth,
    ) {
        browserPageManager.openBrowser(null)
    }
}
