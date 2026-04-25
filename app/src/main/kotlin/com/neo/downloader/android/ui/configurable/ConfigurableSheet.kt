package com.neo.downloader.android.ui.configurable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.neo.downloader.android.ui.SheetHeader
import com.neo.downloader.android.ui.SheetTitle
import com.neo.downloader.android.ui.SheetUI
import com.neo.downloader.shared.ui.widget.Text
import com.neo.downloader.shared.util.OnFullyDismissed
import com.neo.downloader.shared.util.ResponsiveDialog
import com.neo.downloader.shared.util.rememberResponsiveDialogState
import ir.neo.util.compose.StringSource

@Composable
fun ConfigurableSheet(
    title: StringSource,
    isOpened: Boolean,
    onDismiss: () -> Unit,
    headerActions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    val dialogState = rememberResponsiveDialogState(isOpened)
    LaunchedEffect(isOpened) {
        when (isOpened) {
            true -> dialogState.show()
            false -> dialogState.hide()
        }
    }
    dialogState.OnFullyDismissed {
        onDismiss()
    }
    ResponsiveDialog(
        state = dialogState,
        onDismiss = dialogState::hide,
    ) {
        SheetUI(
            header = {
                SheetHeader(
                    headerTitle = {
                        SheetTitle(
                            title.rememberString()
                        )
                    },
                    headerActions = headerActions,
                )
            }
        ) {
            content()
        }
    }
}
