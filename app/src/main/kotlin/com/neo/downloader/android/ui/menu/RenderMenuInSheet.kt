package com.neo.downloader.android.ui.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.neo.downloader.android.ui.SheetHeader
import com.neo.downloader.android.ui.SheetTitle
import com.neo.downloader.android.ui.SheetUI
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.widget.TransparentIconActionButton
import com.neo.downloader.shared.util.OnFullyDismissed
import com.neo.downloader.shared.util.ResponsiveDialog
import com.neo.downloader.shared.util.ResponsiveDialogScope
import com.neo.downloader.shared.util.rememberResponsiveDialogState
import com.neo.downloader.shared.util.ui.icon.MyIcons
import ir.amirab.util.compose.action.MenuItem
import ir.amirab.util.compose.asStringSource

@Composable
private fun ResponsiveDialogScope.RenderMenuInSheetUi(
    menuStack: StackMenuState,
    onDismissRequest: () -> Unit,
) {
    val currentMenu = menuStack.currentMenu
    SheetUI(
        header = {
            SheetHeader(
                headerTitle = {
                    SheetTitle(
                        title = currentMenu.title.collectAsState().value.rememberString(),
                        icon = currentMenu.icon.collectAsState().value,
                    )
                },
                headerActions = {
                    if (menuStack.canGoBack) {
                        TransparentIconActionButton(
                            icon = MyIcons.back,
                            contentDescription = Res.string.back.asStringSource(),
                        ) {
                            menuStack.pop()
                        }
                    }
                    TransparentIconActionButton(
                        MyIcons.close,
                        Res.string.close.asStringSource()
                    ) {
                        onDismissRequest()
                    }
                }
            )
        }
    ) {
        BaseStackedMenu(
            menuStack = menuStack,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun RenderMenuInSheet(
    menu: MenuItem.SubMenu?,
    onDismissRequest: () -> Unit,
) {
    val responsiveDialogState = rememberResponsiveDialogState(false)
    LaunchedEffect(menu) {
        if (menu != null) {
            responsiveDialogState.show()
        } else {
            responsiveDialogState.hide()
        }
    }
    responsiveDialogState.OnFullyDismissed {
        onDismissRequest()
    }
    val hideDialog = responsiveDialogState::hide
    menu?.let {
        ResponsiveDialog(
            responsiveDialogState,
            hideDialog,
        ) {
            val menuStackState = rememberMenuStack(it)
            RenderMenuInSheetUi(menuStackState, hideDialog)
        }
    }
}
